package tonius.emobile.network.message.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.config.EMConfig;
import tonius.emobile.session.CellphoneSessionsManager;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;

public class MessageCellphoneAuthorize implements IMessage, IMessageHandler<MessageCellphoneAuthorize, IMessage> {

    private String acceptingPlayerName;
    private String acceptedPlayerName;

    @SuppressWarnings("unused")
    public MessageCellphoneAuthorize() {
    }

    public MessageCellphoneAuthorize(String acceptingPlayerName, String acceptedPlayerName) {
        this.acceptingPlayerName = acceptingPlayerName;
        this.acceptedPlayerName = acceptedPlayerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.acceptingPlayerName = ByteBufUtils.readUTF8String(buf);
        this.acceptedPlayerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.acceptingPlayerName);
        ByteBufUtils.writeUTF8String(buf, this.acceptedPlayerName);
    }

    @Override
    public IMessage onMessage(MessageCellphoneAuthorize msg, MessageContext ctx) {
        if (!EMConfig.allowTeleportPlayers.getValue()) {
            return null;
        }

        boolean permanent = msg.acceptedPlayerName.startsWith("p:");
        boolean unaccept = msg.acceptedPlayerName.startsWith("!");
        String accepted = msg.acceptedPlayerName.replaceFirst("p:", "").replaceFirst("!", "");

        EntityPlayerMP acceptingPlayer = ServerUtils.getPlayerOnServer(msg.acceptingPlayerName);
        EntityPlayerMP acceptedPlayer = ServerUtils.getPlayerOnServer(accepted);
        if (acceptingPlayer == null) {
            return null;
        }

        if (acceptedPlayer == null) {
            ServerUtils.sendChatToPlayer(acceptingPlayer,
                    StringUtils.translate("chat.cellphone.authorize.unknown", accepted), TextFormatting.RED);
            return null;
        }

        if (acceptingPlayer.equals(acceptedPlayer)) {
            ServerUtils.sendChatToPlayer(acceptingPlayer,
                    StringUtils.translate("chat.cellphone.authorize.self"), TextFormatting.RED);
            return null;
        }

        if (!unaccept) {
            if (CellphoneSessionsManager.acceptPlayer(acceptingPlayer, acceptedPlayer, permanent)) {
                ServerUtils.sendChatToPlayer(acceptingPlayer, StringUtils.translate(
                        "chat.cellphone.authorize.success" + (permanent ? ".permanent" : ""),
                        acceptedPlayer.getName())
                );
            } else {
                ServerUtils.sendChatToPlayer(acceptingPlayer,
                        StringUtils.translate("chat.cellphone.authorize.already", acceptedPlayer.getName()));
            }
        } else {
            if (CellphoneSessionsManager.deacceptPlayer(acceptingPlayer, acceptedPlayer, true)) {
                ServerUtils.sendChatToPlayer(acceptingPlayer,
                        StringUtils.translate("chat.cellphone.unauthorize.success", acceptedPlayer.getName()));
            } else {
                ServerUtils.sendChatToPlayer(acceptingPlayer,
                        StringUtils.translate("chat.cellphone.unauthorize.already", acceptedPlayer.getName()));
            }
        }

        return null;
    }

}
