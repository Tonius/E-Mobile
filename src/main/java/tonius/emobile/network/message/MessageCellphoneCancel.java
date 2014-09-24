package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import tonius.emobile.session.CellphoneSessionsHandler;
import tonius.emobile.util.ServerUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageCellphoneCancel implements IMessage, IMessageHandler<MessageCellphoneCancel, IMessage> {
    
    private String player;
    
    public MessageCellphoneCancel() {
    }
    
    public MessageCellphoneCancel(String player) {
        this.player = player;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = ByteBufUtils.readUTF8String(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.player);
    }
    
    @Override
    public IMessage onMessage(MessageCellphoneCancel msg, MessageContext ctx) {
        EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
        if (player == null) {
            return null;
        } else {
            CellphoneSessionsHandler.cancelSessionsForPlayer(player);
        }
        return null;
    }
    
}
