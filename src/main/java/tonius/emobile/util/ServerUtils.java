package tonius.emobile.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ServerUtils {

    public static EntityPlayerMP getPlayerOnServer(String name) {
        return MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);
    }

    public static void sendGlobalChat(String chat) {
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(chat));
    }

    public static void sendChatToPlayer(String player, String chat, EnumChatFormatting color) {
        EntityPlayerMP playerEntity = getPlayerOnServer(player);
        if (player != null) {
            ChatComponentText component = new ChatComponentText(chat);
            component.getChatStyle().setColor(color);
            playerEntity.addChatMessage(component);
        }
    }

    public static void sendChatToPlayer(String player, String chat) {
        sendChatToPlayer(player, chat, EnumChatFormatting.WHITE);
    }

}
