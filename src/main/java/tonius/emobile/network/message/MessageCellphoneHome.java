package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionLocation;
import tonius.emobile.session.CellphoneSessionsHandler;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class MessageCellphoneHome implements IMessage, IMessageHandler<MessageCellphoneHome, IMessage> {
    
    private String player;
    
    public MessageCellphoneHome() {
    }
    
    public MessageCellphoneHome(String player) {
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
    public IMessage onMessage(MessageCellphoneHome msg, MessageContext ctx) {
        if (EMConfig.allowTeleportHome) {
            EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
            if (player == null) {
                return null;
            } else {
                ChunkCoordinates bed = player.getBedLocation(player.dimension);
                World world = player.worldObj;
                if (bed == null && player.dimension != 0) {
                    if (TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                        bed = player.getBedLocation(0);
                        world = player.mcServer.worldServerForDimension(0);
                    } else {
                        ServerUtils.sendChatToPlayer(player.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimensionName(), player.mcServer.worldServerForDimension(0).provider.getDimensionName()), EnumChatFormatting.RED);
                        return null;
                    }
                }
                if (bed != null) {
                    Block bedBlock = world.getBlock(bed.posX, bed.posY, bed.posZ);
                    if (bedBlock != null && !(bedBlock instanceof BlockBed) && !EMConfig.bedBlocks.contains(bedBlock.getClass().getName())) {
                        bed = null;
                    }
                }
                if (bed != null) {
                    bed = world.getBlock(bed.posX, bed.posY, bed.posZ).getBedSpawnPosition(world, bed.posX, bed.posY, bed.posZ, player);
                }
                if (bed != null) {
                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (player.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).useFuel(heldItem, player)) {
                                ServerUtils.sendDiallingSound(player);
                                new CellphoneSessionLocation(8, "chat.cellphone.location.home", player, 0, bed.posX, bed.posY, bed.posZ);
                            }
                        }
                    }
                } else {
                    ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.translate("chat.cellphone.tryStart.bedmissing.1"), EnumChatFormatting.RED);
                    ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.translate("chat.cellphone.tryStart.bedmissing.2"), EnumChatFormatting.RED);
                }
            }
        }
        
        return null;
    }
}
