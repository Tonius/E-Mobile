package tonius.emobile.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import tonius.emobile.common.config.EMConfig;
import tonius.emobile.common.item.ItemCellphone;
import tonius.emobile.common.util.StringUtils;
import tonius.emobile.server.session.CellphoneSessionPlayer;
import tonius.emobile.server.session.CellphoneSessionsHandler;
import tonius.emobile.server.util.ServerUtils;
import tonius.emobile.server.util.TeleportUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageCellphonePlayer implements IMessage, IMessageHandler<MessageCellphonePlayer, IMessage> {
    
    private String requesting;
    private String receiving;
    
    public MessageCellphonePlayer() {
    }
    
    public MessageCellphonePlayer(String requesting, String receiving) {
        this.requesting = requesting;
        this.receiving = receiving;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        this.requesting = ByteBufUtils.readUTF8String(buf);
        this.receiving = ByteBufUtils.readUTF8String(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.requesting);
        ByteBufUtils.writeUTF8String(buf, this.receiving);
    }
    
    @Override
    public IMessage onMessage(MessageCellphonePlayer msg, MessageContext ctx) {
        if (EMConfig.allowTeleportPlayers) {
            EntityPlayerMP requestingPlayer = ServerUtils.getPlayerOnServer(msg.requesting);
            EntityPlayerMP receivingPlayer = ServerUtils.getPlayerOnServer(msg.receiving);
            if (requestingPlayer == null) {
                return null;
            } else if (receivingPlayer == null) {
                ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.unknown"), msg.receiving), EnumChatFormatting.RED);
            } else if (!TeleportUtils.isDimTeleportAllowed(requestingPlayer.dimension, receivingPlayer.dimension)) {
                ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), requestingPlayer.worldObj.provider.getDimensionName(), receivingPlayer.worldObj.provider.getDimensionName()), EnumChatFormatting.RED);
            } else {
                if (!CellphoneSessionsHandler.isPlayerInSession(requestingPlayer)) {
                    if (CellphoneSessionsHandler.isPlayerAccepted(receivingPlayer, requestingPlayer)) {
                        ItemStack heldItem = requestingPlayer.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (requestingPlayer.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).useFuel(heldItem, requestingPlayer)) {
                                ServerUtils.sendDiallingSound(requestingPlayer);
                                new CellphoneSessionPlayer(8, requestingPlayer, receivingPlayer);
                            }
                        }
                    } else {
                        ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.unauthorized"), receivingPlayer.getCommandSenderName()), EnumChatFormatting.RED);
                    }
                }
            }
        }
        
        return null;
    }
    
}
