package tonius.emobile.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import tonius.emobile.config.EMConfig;

public class TeleportUtils {

    public static void teleportPlayerToPos(EntityPlayerMP player, int dimension, double posX, double posY,
                                           double posZ) {
        if (!DimensionManager.isDimensionRegistered(dimension)) {
            return;
        }

        ServerUtils.sendTeleportEffect(player);

        player.removePassengers();
        player.dismountRidingEntity();

        if (player.dimension != dimension) {
            transferPlayerToDimension(player, dimension, posX, posY, posZ);
        } else {
            player.setPositionAndUpdate(posX, posY, posZ);
        }

        ServerUtils.sendTeleportEffect(player);
    }

    public static void teleportPlayerToPlayer(EntityPlayerMP player, EntityPlayerMP toPlayer) {
        teleportPlayerToPos(player, toPlayer.dimension, toPlayer.posX, toPlayer.posY, toPlayer.posZ);
    }

    public static boolean isDimTeleportAllowed(int from, int to) {
        if (from == to) {
            return true;
        }

        if (!EMConfig.dimensionsWhitelist.getValue()) {
            return !configContainsDim(from) && !configContainsDim(to);
        } else {
            return configContainsDim(from) && configContainsDim(to);
        }
    }

    public static boolean configContainsDim(int dim) {
        for (int i : EMConfig.dimensionsBlacklist.getValue()) {
            if (i == dim) {
                return true;
            }
        }
        return false;
    }

    // This mess is mostly copied from net.minecraft.server.management.PlayerList, but needed some changes for E-Mobile
    private static void transferPlayerToDimension(EntityPlayerMP player, int toDimension, double posX, double posY,
                                                  double posZ) {
        MinecraftServer server = ServerUtils.getServer();
        PlayerList playerList = server.getPlayerList();

        int lastDimension = player.dimension;
        player.dimension = toDimension;

        WorldServer lastWorld = server.worldServerForDimension(lastDimension);
        WorldServer toWorld = server.worldServerForDimension(toDimension);

        player.connection.sendPacket(new SPacketRespawn(player.dimension, toWorld.getDifficulty(),
                toWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
        playerList.updatePermissionLevel(player);

        lastWorld.removeEntityDangerously(player);
        player.isDead = false;

        player.setLocationAndAngles(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
        toWorld.spawnEntityInWorld(player);
        toWorld.updateEntityWithOptionalForce(player, false);
        player.setWorld(toWorld);

        playerList.preparePlayer(player, lastWorld);
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw,
                player.rotationPitch);
        player.interactionManager.setWorld(toWorld);
        player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
        playerList.updateTimeAndWeatherForPlayer(player, toWorld);
        playerList.syncPlayerInventory(player);

        for (PotionEffect potioneffect : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }

        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, lastDimension,
                toDimension);
    }

}
