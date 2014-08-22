package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionLocation;
import tonius.emobile.session.CellphoneSessionsHandler;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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
                if (bed == null && player.dimension != 0)
                    if (TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                        bed = player.getBedLocation(0);
                        world = player.mcServer.worldServerForDimension(0);
                    } else {
                        ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.LIGHT_RED + String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimensionName(), player.mcServer.worldServerForDimension(0).provider.getDimensionName()));
                        return null;
                    }
                if (!(world.getBlock(bed.posX, bed.posY, bed.posZ) instanceof BlockBed))
                    bed = null;
                if (bed != null)
                    bed = world.getBlock(bed.posX, bed.posY, bed.posZ).getBedSpawnPosition(world, bed.posX, bed.posY, bed.posZ, player);
                if (bed != null) {
                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (((ItemCellphone) heldItem.getItem()).usePearl(heldItem, player)) {
                                player.worldObj.playSoundAtEntity(player, "emobile:phonecountdown", 1.0F, 1.0F);
                                new CellphoneSessionLocation(8, "chat.cellphone.location.home", player, 0, bed.posX, bed.posY, bed.posZ);
                            }
                        }
                    }
                } else {
                    ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.LIGHT_RED + StringUtils.translate("chat.cellphone.tryStart.bedmissing.1"));
                    ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.LIGHT_RED + StringUtils.translate("chat.cellphone.tryStart.bedmissing.2"));
                }
            }
        }

        return null;
    }
}
