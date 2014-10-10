package tonius.emobile.common.network.message;

import io.netty.buffer.ByteBuf;
import tonius.emobile.common.EMobile;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageTeleportParticles implements IMessage, IMessageHandler<MessageTeleportParticles, IMessage> {
    
    public double posX;
    public double posY;
    public double posZ;
    
    public MessageTeleportParticles() {
    }
    
    public MessageTeleportParticles(double posX, double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
    }
    
    @Override
    public IMessage onMessage(MessageTeleportParticles msg, MessageContext ctx) {
        EMobile.proxy.showTeleportParticles(msg.posX, msg.posY, msg.posZ);
        return null;
    }
    
}
