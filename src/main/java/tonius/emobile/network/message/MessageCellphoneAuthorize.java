package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.config.EMConfig;
import tonius.emobile.session.CellphoneSessionsManager;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;

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

            EntityPlayerMP acceptingPlayer = ServerUtils.getPlayerOnServer(msg.accepting);
            EntityPlayerMP acceptedPlayer = ServerUtils.getPlayerOnServer(accepted);
            if (acceptingPlayer == null) {
                return null;
            } else if (acceptedPlayer == null) {
                ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.authorize.unknown"), accepted), EnumChatFormatting.RED);
            } else if (acceptingPlayer.equals(acceptedPlayer)) {
                ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), StringUtils.translate("chat.cellphone.authorize.self"), EnumChatFormatting.RED);
            } else {
                if (!unaccept) {
                    if (CellphoneSessionsManager.acceptPlayer(acceptingPlayer, acceptedPlayer, perma)) {
                        ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.authorize.success" + (perma ? ".perma" : "")), acceptedPlayer.getCommandSenderName()));
                    } else {
                        ServerUtils.sendChatToPlayer(acceptingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.authorize.already"), acceptedPlayer.getCommandSenderName()));
                    }
                } else {
                    if (CellphoneSessionsManager.deacceptPlayer(acceptingPlayer, acceptedPlayer, true)) {
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
