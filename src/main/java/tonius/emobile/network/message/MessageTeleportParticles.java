package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageTeleportParticles implements IMessage, IMessageHandler<MessageTeleportParticles, IMessage> {
    
    public int entityID;
    
    public MessageTeleportParticles() {
    }
    
    public MessageTeleportParticles(int entityID) {
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
    public IMessage onMessage(MessageTeleportParticles msg, MessageContext ctx) {
        Entity entity = FMLClientHandler.instance().getClient().theWorld.getEntityByID(msg.entityID);
        if (entity instanceof EntityPlayer) {
            Random rand = ((EntityPlayer) entity).getRNG();
            for (int i = 0; i <= 150; i++) {
                double velX = -rand.nextGaussian() * 0.2D;
                double velY = -rand.nextGaussian() * 0.2D;
                double velZ = -rand.nextGaussian() * 0.2D;
                entity.worldObj.spawnParticle("explode", entity.posX, entity.posY - 0.5D, entity.posZ, velX, velY, velZ);
            }
        }
        return null;
    }
    
}
