package tonius.emobile.network.message;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tonius.emobile.EMobile;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TeleportParticle implements IMessage, IMessageHandler<TeleportParticle, IMessage> {
	int entityID;
	int amount = 100;
	Random rand = new Random();
	
	public TeleportParticle(){
	}
	
	 public TeleportParticle(int entityID) {
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
	    public IMessage onMessage(TeleportParticle msg, MessageContext ctx) {
	        Entity entity = FMLClientHandler.instance().getClient().theWorld.getEntityByID(msg.entityID);
	        if (entity instanceof EntityPlayer) {
	        	for (int i = 0; i < this.amount; i++){
	        		
	        		double rand1 = rand.nextGaussian();
	        		double rand2 = rand.nextGaussian();
	        		double rand3 = rand.nextGaussian();
	        		entity.worldObj.spawnParticle("portal", entity.posX, entity.posY, entity.posZ, rand1 , rand2 , rand3 );
	        	}
	        }
	        return null;
	    }

}
