package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tonius.emobile.audio.SoundDialling;
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
        World world = FMLClientHandler.instance().getClient().theWorld;
        if (world != null) {
            Entity entity = world.getEntityByID(msg.entityID);
            if (entity instanceof EntityPlayer) {
                FMLClientHandler.instance().getClient().getSoundHandler().playSound(new SoundDialling((EntityPlayer) entity));
            }
        }
        return null;
    }
    
}
