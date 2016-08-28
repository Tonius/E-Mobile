package tonius.emobile.network.message.toclient;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.EMobile;

public class MessageDiallingSound implements IMessage, IMessageHandler<MessageDiallingSound, IMessage> {

    public int entityID;

    @SuppressWarnings("unused")
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
