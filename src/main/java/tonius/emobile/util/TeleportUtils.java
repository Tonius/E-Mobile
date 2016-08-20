package tonius.emobile.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import tonius.emobile.config.EMConfig;

import java.util.Iterator;

public class TeleportUtils {
    
    public static void teleportPlayerToPlayer(EntityPlayerMP from, EntityPlayerMP to) {
        from.mountEntity(null);
        if (from.riddenByEntity != null) {
            from.riddenByEntity.mountEntity(null);
        }
        if (from.dimension != to.dimension) {
            teleportPlayerToDimension(from, to.dimension, to.mcServer.getConfigurationManager());
        }
        from.setPositionAndUpdate(to.posX + 0.5D, to.posY + 0.5D, to.posZ + 0.5D);
    }
    
    public static boolean teleportPlayerToPos(EntityPlayerMP player, int dimension, int posX, int posY, int posZ, boolean simulate) {
        if (!DimensionManager.isDimensionRegistered(dimension)) {
            return false;
        }
        
        if (!simulate) {
            player.mountEntity(null);
            if (player.riddenByEntity != null) {
                player.riddenByEntity.mountEntity(null);
            }
            if (player.dimension != dimension) {
                teleportPlayerToDimension(player, dimension, player.mcServer.getConfigurationManager());
            }
            player.setPositionAndUpdate(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
        }
        
        return true;
    }
    
    public static boolean isDimTeleportAllowed(int from, int to) {
        if (from == to) {
            return true;
        }
        
        if (!EMConfig.dimensionsWhitelist) {
            if (configContainsDim(from) || configContainsDim(to)) {
                return false;
            }
            return true;
        } else {
            if (configContainsDim(from) && configContainsDim(to)) {
                return true;
            }
            return false;
        }
    }
    
    public static boolean configContainsDim(int dim) {
        for (int i : EMConfig.dimensionsBlacklist) {
            if (i == dim) {
                return true;
            }
        }
        return false;
    }
    
    public static void teleportEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld) {
        WorldProvider pOld = oldWorld.provider;
        WorldProvider pNew = newWorld.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double x = entity.posX * moveFactor;
        double z = entity.posZ * moveFactor;
        
        oldWorld.theProfiler.startSection("placing");
        x = MathHelper.clamp_double(x, -29999872, 29999872);
        z = MathHelper.clamp_double(z, -29999872, 29999872);
        
        if (entity.isEntityAlive()) {
            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            newWorld.spawnEntityInWorld(entity);
            newWorld.updateEntityWithOptionalForce(entity, false);
        }
        
        oldWorld.theProfiler.endSection();
        
        entity.setWorld(newWorld);
    }
    
    public static void teleportPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager manager) {
        int oldDim = player.dimension;
        WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(player);
        player.isDead = false;
        teleportEntityToWorld(player, worldserver, worldserver1);
        manager.func_72375_a(player, worldserver);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldserver1);
        manager.updateTimeAndWeatherForPlayer(player, worldserver1);
        manager.syncPlayerInventory(player);
        Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
        
        while (iterator.hasNext()) {
            PotionEffect potioneffect = iterator.next();
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }
    
}
