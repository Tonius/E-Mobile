package tonius.emobile.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tonius.emobile.common.EMobile;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageDiallingSound implements IMessage, IMessageHandler<MessageDiallingSound, IMessage> {
    
    public int entityID;
    
    public MessageDiallingSound() {
    }
    
    public MessageDiallingSound(int entityID) {
        this.entityID = entityID;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
    }
    
    @Override
    public IMessage onMessage(MessageDiallingSound msg, MessageContext ctx) {
        Entity entity = FMLClientHandler.instance().getClient().theWorld.getEntityByID(msg.entityID);
        if (entity instanceof EntityPlayer) {
            EMobile.proxy.playDiallingSound((EntityPlayer) entity);
        }
        return null;
    }
    
}
