package tonius.emobile.common.session;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tonius.emobile.common.util.ServerUtils;
import tonius.emobile.common.util.StringUtils;
import tonius.emobile.common.util.TeleportUtils;

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
        if (this.requestingPlayer == null || this.receivingPlayer == null || !ServerUtils.isPlayerConnected(this.requestingPlayer) || !ServerUtils.isPlayerConnected(this.receivingPlayer)) {
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
        ServerUtils.sendChatToPlayer(this.requestingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.cancel.player"), canceledBy), EnumChatFormatting.RED);
        ServerUtils.sendChatToPlayer(this.receivingPlayer.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.cancel.player"), canceledBy), EnumChatFormatting.RED);
    }
    
    @Override
    public boolean isPlayerInSession(EntityPlayer player) {
        return player.equals(this.requestingPlayer) || player.equals(this.receivingPlayer);
    }
    
}
