package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionLocation;
import tonius.emobile.session.CellphoneSessionsHandler;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
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
            } else if (player.worldObj.provider.dimensionId != 0) {
                ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.LIGHT_RED + StringUtils.translate("chat.cellphone.tryStart.overworld"));
                return null;
            } else {
                ChunkCoordinates spawn = player.worldObj.getSpawnPoint();
                if (spawn != null) {
                    Material mat;
                    do {
                        mat = player.worldObj.getBlock(spawn.posX, spawn.posY, spawn.posZ).getMaterial();
                        if (!mat.isSolid() && !mat.isLiquid()) {
                            break;
                        }
                        spawn.posY++;
                    } while (mat.isSolid() || mat.isLiquid());
                    spawn.posY += 0.2;

                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (((ItemCellphone) heldItem.getItem()).usePearl(heldItem, player)) {
                                player.worldObj.playSoundAtEntity(player, "emobile:phonecountdown", 1.0F, 1.0F);
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
