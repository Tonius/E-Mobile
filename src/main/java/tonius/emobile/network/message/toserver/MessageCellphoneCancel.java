package tonius.emobile.network.message.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.session.CellphoneSessionsManager;
import tonius.emobile.util.ServerUtils;

public class MessageCellphoneCancel implements IMessage, IMessageHandler<MessageCellphoneCancel, IMessage> {

    private String playerName;

    @SuppressWarnings("unused")
    public MessageCellphoneCancel() {
    }

    public MessageCellphoneCancel(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.playerName);
    }

    @Override
    public IMessage onMessage(MessageCellphoneCancel msg, MessageContext ctx) {
        EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.playerName);
        if (player == null) {
            return null;
        }

        CellphoneSessionsManager.cancelSessionsForPlayer(player);

        return null;
    }

}
