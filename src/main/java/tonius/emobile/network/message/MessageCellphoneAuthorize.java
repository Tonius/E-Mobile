package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import tonius.emobile.config.EMConfig;
import tonius.emobile.session.CellphoneSessionsHandler;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageCellphoneAuthorize implements IMessage, IMessageHandler<MessageCellphoneAuthorize, IMessage> {

    private String accepting;
    private String accepted;

    public MessageCellphoneAuthorize() {
    }

    public MessageCellphoneAuthorize(String accepting, String accepted) {
        this.accepting = accepting;
        this.accepted = accepted;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.accepting = ByteBufUtils.readUTF8String(buf);
        this.accepted = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.accepting);
        ByteBufUtils.writeUTF8String(buf, this.accepted);
    }

    @Override
    public IMessage onMessage(MessageCellphoneAuthorize msg, MessageContext ctx) {
        if (EMConfig.allowTeleportPlayers) {
            boolean perma = msg.accepted.startsWith("p:");
            boolean unaccept = msg.accepted.startsWith("!");
            String accepted = msg.accepted.replaceFirst("p:", "").replaceFirst("!", "");

            System.out.println("Read name: " + accepted + ", perma: " + perma + ", unaccept: " + unaccept);

            EntityPlayerMP acceptingPlayer = ServerUtils.getPlayerOnServer(msg.accepting);
            EntityPlayerMP acceptedPlayer = ServerUtils.getPlayerOnServer(accepted);
            if (acceptingPlayer == null) {
                return null;
            } else if (acceptedPlayer == null) {
                ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), StringUtils.LIGHT_RED + String.format(StringUtils.translate("chat.cellphone.authorize.unknown"), accepted));
            } else if (acceptingPlayer.equals(acceptedPlayer)) {
                ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), StringUtils.LIGHT_RED + StringUtils.translate("chat.cellphone.authorize.self"));
            } else {
                if (!unaccept) {
                    if (CellphoneSessionsHandler.acceptPlayer(acceptingPlayer, acceptedPlayer, perma)) {
                        if (perma) {
                            ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.authorize.success.perma"), acceptedPlayer.getCommandSenderName()));
                        } else {
                            ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.authorize.success"), acceptedPlayer.getCommandSenderName()));
                        }
                    } else {
                        ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.authorize.already"), acceptedPlayer.getCommandSenderName()));
                    }
                } else {
                    if (CellphoneSessionsHandler.deacceptPlayer(acceptingPlayer, acceptedPlayer, true)) {
                        ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.unauthorize.success"), acceptedPlayer.getCommandSenderName()));
                    } else {
                        ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.unauthorize.already"), acceptedPlayer.getCommandSenderName()));
                    }
                }
            }
        }

        return null;
    }

}
