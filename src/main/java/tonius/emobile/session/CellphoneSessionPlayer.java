package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class CellphoneSessionPlayer extends CellphoneSessionBase {
    
    protected EntityPlayerMP requestingPlayer;
    protected EntityPlayerMP receivingPlayer;
    
    public CellphoneSessionPlayer(int duration, EntityPlayerMP requestingPlayer, EntityPlayerMP receivingPlayer) {
        super(duration);
        this.requestingPlayer = requestingPlayer;
        this.receivingPlayer = receivingPlayer;
        
        ServerUtils.sendChatToPlayer(requestingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.start.requesting"), receivingPlayer.getCommandSenderName()), EnumChatFormatting.GOLD);
        ServerUtils.sendChatToPlayer(receivingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.start.receiving"), requestingPlayer.getCommandSenderName()), EnumChatFormatting.GOLD);
    }
    
    @Override
    public void tick() {
        if (!ServerUtils.canPlayerTeleport(this.requestingPlayer) || !ServerUtils.canPlayerTeleport(this.receivingPlayer)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isDimTeleportAllowed(this.requestingPlayer.dimension, this.receivingPlayer.dimension)) {
            String msg = String.format(StringUtils.translate("chat.cellphone.cancel.dimension"), this.requestingPlayer.worldObj.provider.getDimensionName(), this.receivingPlayer.worldObj.provider.getDimensionName());
            ServerUtils.sendChatToPlayer(this.requestingPlayer.getCommandSenderName(), msg, EnumChatFormatting.RED);
            ServerUtils.sendChatToPlayer(this.receivingPlayer.getCommandSenderName(), msg, EnumChatFormatting.RED);
            this.invalidate();
            return;
        }
        
        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.sendDiallingParticles(this.receivingPlayer);
            ServerUtils.sendDiallingParticles(this.requestingPlayer);
        }
        
        super.tick();
    }
    
    @Override
    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            this.requestingPlayer.addChatMessage(new ChatComponentText(StringUtils.PINK + String.format(StringUtils.translate("chat.cellphone.countdown"), this.countdownSecs)));
        }
    }
    
    @Override
    public void onCountdownFinished() {
        this.requestingPlayer.worldObj.playSoundAtEntity(this.requestingPlayer, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.receivingPlayer);
        TeleportUtils.teleportPlayerToPlayer(this.requestingPlayer, this.receivingPlayer);
        this.requestingPlayer.worldObj.playSoundAtEntity(this.requestingPlayer, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.receivingPlayer);
        ServerUtils.sendChatToPlayer(this.requestingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.success.requesting"), this.receivingPlayer.getCommandSenderName()), EnumChatFormatting.GOLD);
        ServerUtils.sendChatToPlayer(this.receivingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.success.receiving"), this.requestingPlayer.getCommandSenderName()), EnumChatFormatting.GOLD);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        CellphoneSessionsHandler.deacceptPlayer(this.receivingPlayer, this.requestingPlayer, false);
    }
    
    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);
        String msg = String.format(StringUtils.translate("chat.cellphone.cancel.player"), canceledBy);
        ServerUtils.sendChatToPlayer(this.requestingPlayer.getCommandSenderName(), msg, EnumChatFormatting.RED);
        ServerUtils.sendChatToPlayer(this.receivingPlayer.getCommandSenderName(), msg, EnumChatFormatting.RED);
    }
    
    @Override
    public boolean isPlayerInSession(EntityPlayer player) {
        return player.equals(this.requestingPlayer) || player.equals(this.receivingPlayer);
    }
    
}
