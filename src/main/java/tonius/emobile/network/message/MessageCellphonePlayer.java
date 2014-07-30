package tonius.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionPlayer;
import tonius.emobile.session.CellphoneSessionsHandler;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
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
                ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), StringUtils.LIGHT_RED + String.format(StringUtils.translate("chat.cellphone.tryStart.unknown"), msg.receiving));
            } else if (receivingPlayer.equals(requestingPlayer)) {
                ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), StringUtils.LIGHT_RED + StringUtils.translate("chat.cellphone.tryStart.self"));
            } else if (requestingPlayer.worldObj.provider.dimensionId != receivingPlayer.worldObj.provider.dimensionId) {
                ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), StringUtils.LIGHT_RED + String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), receivingPlayer.getCommandSenderName()));
            } else {
                if (!CellphoneSessionsHandler.isPlayerInSession(requestingPlayer)) {
                    if (CellphoneSessionsHandler.isPlayerAccepted(receivingPlayer, requestingPlayer)) {
                        ItemStack heldItem = requestingPlayer.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (((ItemCellphone) heldItem.getItem()).usePearl(heldItem, requestingPlayer)) {
                                requestingPlayer.worldObj.playSoundAtEntity(requestingPlayer, "emobile:phonecountdown", 1.0F, 1.0F);
                                new CellphoneSessionPlayer(8, requestingPlayer, receivingPlayer);
                            }
                        }
                    } else {
                        ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), StringUtils.LIGHT_RED + String.format(StringUtils.translate("chat.cellphone.tryStart.unauthorized"), receivingPlayer.getCommandSenderName()));
                    }
                }
            }
        }

        return null;
    }

}
