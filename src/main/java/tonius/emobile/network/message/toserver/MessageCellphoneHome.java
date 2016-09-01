package tonius.emobile.network.message.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tonius.emobile.config.EMConfig;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.session.CellphoneSessionLocation;
import tonius.emobile.session.CellphoneSessionsManager;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

import java.util.Arrays;

public class MessageCellphoneHome implements IMessage, IMessageHandler<MessageCellphoneHome, IMessage> {

    private String playerName;

    @SuppressWarnings("unused")
    public MessageCellphoneHome() {
    }

    public MessageCellphoneHome(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.playerName);
    }

    @Override
    public IMessage onMessage(MessageCellphoneHome msg, MessageContext ctx) {
        if (!EMConfig.allowTeleportHome.getValue()) {
            return null;
        }

        EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.playerName);
        if (player == null || CellphoneSessionsManager.isPlayerInSession(player) || !ItemCellphone.tryUseFuel(player)) {
            return null;
        }

        int bedDimension = player.dimension;
        BlockPos bedPos = player.getBedLocation(bedDimension);
        World world = player.worldObj;

        //noinspection ConstantConditions
        if (bedPos == null && player.dimension != 0) {
            if (TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                bedDimension = 0;
                bedPos = player.getBedLocation(bedDimension);
                world = player.mcServer.worldServerForDimension(bedDimension);
            } else {
                ServerUtils.sendChatToPlayer(player, StringUtils.translate(
                        "chat.cellphone.tryStart.dimension",
                        player.worldObj.provider.getDimensionType().getName(),
                        player.mcServer.worldServerForDimension(0).provider.getDimensionType().getName()
                ), TextFormatting.RED);

                return null;
            }
        }

        //noinspection ConstantConditions
        if (bedPos != null) {
            Block bedBlock = world.getBlockState(bedPos).getBlock();
            if (!(bedBlock instanceof BlockBed) &&
                    !Arrays.asList(EMConfig.bedBlocks.getValue()).contains(bedBlock.getClass().getName())) {
                bedPos = null;
            }
        }

        if (bedPos != null) {
            bedPos = world.getBlockState(bedPos).getBlock()
                    .getBedSpawnPosition(world.getBlockState(bedPos), world, bedPos, player);
        }

        if (bedPos == null) {
            ServerUtils.sendChatToPlayer(player,
                    StringUtils.translate("chat.cellphone.tryStart.bedmissing.1"), TextFormatting.RED);
            ServerUtils.sendChatToPlayer(player,
                    StringUtils.translate("chat.cellphone.tryStart.bedmissing.2"), TextFormatting.RED);
            return null;
        }

        CellphoneSessionsManager.addSession(new CellphoneSessionLocation(
                player, "chat.cellphone.location.home", bedDimension, bedPos.getX(), bedPos.getY(), bedPos.getZ()
        ));

        return null;
    }
}
