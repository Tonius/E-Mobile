package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class CellphoneSessionLocation extends CellphoneSessionBase {
    
    protected EntityPlayerMP player;
    protected String unlocalizedLocation;
    protected int dimension;
    protected int posX;
    protected int posY;
    protected int posZ;
    
    public CellphoneSessionLocation(int duration, String unlocalizedLocation, EntityPlayerMP player, int dimension, int posX, int posY, int posZ) {
        super(duration);
        this.unlocalizedLocation = unlocalizedLocation;
        this.player = player;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        
        ServerUtils.sendChatToPlayer(player.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.start.location"), StringUtils.translate(unlocalizedLocation)), EnumChatFormatting.GOLD);
    }
    
    @Override
    public void tick() {
        if (!ServerUtils.canPlayerTeleport(this.player)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isDimTeleportAllowed(this.player.dimension, this.dimension)) {
            String msg = String.format(StringUtils.translate("chat.cellphone.cancel.dimension"), this.player.worldObj.provider.getDimensionName(), this.player.mcServer.worldServerForDimension(this.dimension).provider.getDimensionName());
            ServerUtils.sendChatToPlayer(this.player.getCommandSenderName(), msg, EnumChatFormatting.RED);
            this.invalidate();
            return;
        }
        
        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.sendDiallingParticles(this.player);
            ServerUtils.sendDiallingParticles(this.dimension, this.posX, this.posY, this.posZ);
        }
        
        super.tick();
    }
    
    @Override
    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            this.player.addChatMessage(new ChatComponentText(StringUtils.PINK + String.format(StringUtils.translate("chat.cellphone.countdown"), this.countdownSecs)));
        }
    }
    
    @Override
    public void onCountdownFinished() {
        this.player.worldObj.playSoundAtEntity(this.player, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.player);
        TeleportUtils.teleportPlayerToPos(this.player, this.dimension, this.posX, this.posY, this.posZ, false);
        this.player.worldObj.playSoundAtEntity(this.player, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.player);
        ServerUtils.sendChatToPlayer(this.player.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.success.location"), StringUtils.translate(this.unlocalizedLocation)), EnumChatFormatting.GOLD);
    }
    
    @Override
    public boolean isPlayerInSession(EntityPlayer player) {
        return this.player.equals(player);
    }
    
    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);
        ServerUtils.sendChatToPlayer(this.player.getCommandSenderName(), StringUtils.translate("chat.cellphone.cancel"), EnumChatFormatting.RED);
    }
    
}
