package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class CellphoneSessionLocation extends CellphoneSessionBase {

    protected String unlocalizedLocation;
    protected int dimension;
    protected int posX;
    protected int posY;
    protected int posZ;

    public CellphoneSessionLocation(int duration, EntityPlayerMP player, String unlocalizedLocation, int dimension,
                                    int posX, int posY, int posZ) {
        super(duration, player);

        this.unlocalizedLocation = unlocalizedLocation;
        this.player = player;
        this.dimension = dimension;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;

        ServerUtils.sendChatToPlayer(player, StringUtils.translate("chat.cellphone.start.location",
                StringUtils.translate(unlocalizedLocation)), TextFormatting.GOLD);
    }

    @Override
    public void tick() {
        if (!ServerUtils.isPlayerAlive(this.player)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isDimTeleportAllowed(this.player.dimension, this.dimension)) {
            ServerUtils.sendChatToPlayer(this.player, StringUtils.translate(
                    "chat.cellphone.cancel.dimension",
                    this.player.worldObj.provider.getDimensionType().getName(),
                    this.player.mcServer.worldServerForDimension(this.dimension).provider.getDimensionType().getName()
            ), TextFormatting.RED);

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
    public void onCountdownFinished() {
        TeleportUtils.teleportPlayerToPos(this.player, this.dimension,
                this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);

        ServerUtils.sendChatToPlayer(this.player, StringUtils.translate(
                "chat.cellphone.success.location", StringUtils.translate(this.unlocalizedLocation)
        ), TextFormatting.GOLD);
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);

        ServerUtils.sendChatToPlayer(this.player, StringUtils.translate("chat.cellphone.cancel"), TextFormatting.RED);
    }

}
