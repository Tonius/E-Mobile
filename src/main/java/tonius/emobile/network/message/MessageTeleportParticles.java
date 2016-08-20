package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.EMobile;

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
