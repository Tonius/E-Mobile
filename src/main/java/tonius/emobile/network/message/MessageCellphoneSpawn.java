package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
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

public class MessageCellphoneSpawn implements IMessage, IMessageHandler<MessageCellphoneSpawn, IMessage> {
    
    private String player;
    
    public MessageCellphoneSpawn() {
    }
    
    public MessageCellphoneSpawn(String player) {
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
    public IMessage onMessage(MessageCellphoneSpawn msg, MessageContext ctx) {
        if (EMConfig.allowTeleportSpawn) {
            EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
            if (player == null) {
                return null;
            } else if (!TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                ServerUtils.sendChatToPlayer(player.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimensionName(), player.mcServer.worldServerForDimension(0).provider.getDimensionName()), EnumChatFormatting.RED);
            } else {
                World world = player.mcServer.worldServerForDimension(0);
                ChunkCoordinates spawn = world.getSpawnPoint();
                if (player.worldObj.provider.canRespawnHere()) {
                    world = player.worldObj;
                    spawn = world.getSpawnPoint();
                }
                if (spawn != null) {
                    spawn.posY = world.provider.getAverageGroundLevel();
                    Material mat = world.getBlock(spawn.posX, spawn.posY, spawn.posZ).getMaterial();
                    Material mat2 = world.getBlock(spawn.posX, spawn.posY + 1, spawn.posZ).getMaterial();
                    if (mat.isSolid() || mat.isLiquid() || mat2.isSolid() || mat2.isLiquid()) {
                        do {
                            mat = world.getBlock(spawn.posX, spawn.posY, spawn.posZ).getMaterial();
                            mat2 = world.getBlock(spawn.posX, spawn.posY + 1, spawn.posZ).getMaterial();
                            if (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid()) {
                                break;
                            }
                            spawn.posY++;
                        } while (mat.isSolid() || mat.isLiquid() || mat2.isSolid() || mat2.isLiquid());
                    } else {
                        do {
                            mat = world.getBlock(spawn.posX, spawn.posY - 1, spawn.posZ).getMaterial();
                            mat2 = world.getBlock(spawn.posX, spawn.posY - 2, spawn.posZ).getMaterial();
                            if ((mat.isSolid() || mat.isLiquid()) && (mat2.isSolid() || mat2.isLiquid())) {
                                break;
                            }
                            spawn.posY--;
                        } while (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid());
                    }
                    spawn.posY += 0.2D;
                    
                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (player.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).usePearl(heldItem, player)) {
                                ServerUtils.sendDiallingSound(player);
                                new CellphoneSessionLocation(8, "chat.cellphone.location.spawn", player, 0, spawn.posX, spawn.posY, spawn.posZ);
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
}
