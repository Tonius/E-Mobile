package tonius.emobile.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.network.message.MessageDiallingParticles;
import tonius.emobile.network.message.MessageDiallingSound;
import tonius.emobile.network.message.MessageTeleportParticles;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class ServerUtils {
    
    public static EntityPlayerMP getPlayerOnServer(String name) {
        return MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);
    }
    
    public static boolean isPlayerConnected(EntityPlayerMP player) {
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
    }
    
    public static void sendGlobalChat(String chat) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(chat));
    }
    
    public static void sendChatToPlayer(String player, String chat, EnumChatFormatting color) {
        EntityPlayerMP playerEntity = getPlayerOnServer(player);
        if (playerEntity != null) {
            ChatComponentText component = new ChatComponentText(chat);
            component.getChatStyle().setColor(color);
            playerEntity.addChatMessage(component);
        }
    }
    
    public static void sendChatToPlayer(String player, String chat) {
        sendChatToPlayer(player, chat, EnumChatFormatting.WHITE);
    }
    
    public static void sendDiallingSound(EntityPlayer player) {
        PacketHandler.instance.sendToAllAround(new MessageDiallingSound(player.getEntityId()), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64D));
    }
    
    public static void sendDiallingParticles(EntityPlayer player) {
        PacketHandler.instance.sendToAllAround(new MessageDiallingParticles(player.getEntityId()), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64D));
    }
    
    public static void sendTeleportParticles(EntityPlayer player) {
        PacketHandler.instance.sendToAllAround(new MessageTeleportParticles(player.getEntityId()), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64D));
    }
    
}
