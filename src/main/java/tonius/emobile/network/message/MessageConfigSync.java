package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import tonius.emobile.EMobile;
import tonius.emobile.config.EMConfig;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageConfigSync implements IMessage, IMessageHandler<MessageConfigSync, IMessage> {
    
    public boolean allowTeleportPlayers;
    public boolean allowTeleportHome;
    public boolean allowTeleportSpawn;
    public int fluxCellphoneMaxEnergy;
    public int fluxCellphoneMaxInput;
    public int fluxCellphoneEnergyPerUse;
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.allowTeleportPlayers = buf.readBoolean();
        this.allowTeleportHome = buf.readBoolean();
        this.allowTeleportSpawn = buf.readBoolean();
        this.fluxCellphoneMaxEnergy = buf.readInt();
        this.fluxCellphoneMaxInput = buf.readInt();
        this.fluxCellphoneEnergyPerUse = buf.readInt();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(EMConfig.allowTeleportPlayers);
        buf.writeBoolean(EMConfig.allowTeleportHome);
        buf.writeBoolean(EMConfig.allowTeleportSpawn);
        buf.writeInt(EMConfig.fluxCellphoneMaxEnergy);
        buf.writeInt(EMConfig.fluxCellphoneMaxInput);
        buf.writeInt(EMConfig.fluxCellphoneEnergyPerUse);
    }
    
    @Override
    public IMessage onMessage(MessageConfigSync msg, MessageContext ctx) {
        EMConfig.allowTeleportPlayers = msg.allowTeleportPlayers;
        EMConfig.allowTeleportHome = msg.allowTeleportHome;
        EMConfig.allowTeleportSpawn = msg.allowTeleportSpawn;
        if (EMobile.cellphoneRF != null) {
            EMobile.cellphoneRF.maxEnergy = msg.fluxCellphoneMaxEnergy;
            EMobile.cellphoneRF.maxInput = msg.fluxCellphoneMaxInput;
            EMobile.cellphoneRF.energyPerUse = msg.fluxCellphoneEnergyPerUse;
        }
        
        EMobile.logger.info("Received server configuration");
        return null;
    }
    
}
