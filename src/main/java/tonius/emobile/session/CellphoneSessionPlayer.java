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
        if (this.requestingPlayer == null || this.receivingPlayer == null || !ServerUtils.isPlayerConnected(this.requestingPlayer) || !ServerUtils.isPlayerConnected(this.receivingPlayer)) {
            this.invalidate();
            return;
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
        this.requestingPlayer.worldObj.playSoundAtEntity(this.requestingPlayer, "mob.chicken.plop", 1.0F, 1.0F);
        TeleportUtils.teleportPlayerToPlayer(this.requestingPlayer, this.receivingPlayer);
        this.requestingPlayer.worldObj.playSoundAtEntity(this.requestingPlayer, "mob.chicken.plop", 1.0F, 1.0F);
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
