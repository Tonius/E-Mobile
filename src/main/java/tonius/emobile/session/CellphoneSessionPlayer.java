package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import tonius.emobile.util.ServerUtils;
import tonius.emobile.util.StringUtils;
import tonius.emobile.util.TeleportUtils;

public class CellphoneSessionPlayer extends CellphoneSessionBase {

    protected EntityPlayerMP toPlayer;

    public CellphoneSessionPlayer(EntityPlayerMP player, EntityPlayerMP toPlayer) {
        super(player);

        this.toPlayer = toPlayer;

        ServerUtils.sendChatToPlayer(this.player, StringUtils.translate(
                "chat.cellphone.start.requesting", this.toPlayer.getName()
        ), TextFormatting.GOLD);

        ServerUtils.sendChatToPlayer(this.toPlayer, StringUtils.translate(
                "chat.cellphone.start.receiving", this.player.getName()
        ), TextFormatting.GOLD);
    }

    @Override
    public void tick() {
        if (!ServerUtils.isPlayerAlive(this.player) || !ServerUtils.isPlayerAlive(this.toPlayer)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isDimTeleportAllowed(this.player.dimension, this.toPlayer.dimension)) {
            String msg = StringUtils.translate(
                    "chat.cellphone.cancel.dimension",
                    this.player.worldObj.provider.getDimensionType().getName(),
                    this.toPlayer.worldObj.provider.getDimensionType().getName()
            );
            ServerUtils.sendChatToPlayer(this.player, msg, TextFormatting.RED);
            ServerUtils.sendChatToPlayer(this.toPlayer, msg, TextFormatting.RED);

            this.invalidate();
            return;
        }

        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.sendDiallingParticles(this.toPlayer);
            ServerUtils.sendDiallingParticles(this.player);
        }

        super.tick();
    }

    @Override
    public void onCountdownFinished() {
        TeleportUtils.teleportPlayerToPlayer(this.player, this.toPlayer);

        ServerUtils.sendChatToPlayer(this.player, StringUtils.translate(
                "chat.cellphone.success.requesting", this.toPlayer.getName()
        ), TextFormatting.GOLD);

        ServerUtils.sendChatToPlayer(this.toPlayer, StringUtils.translate(
                "chat.cellphone.success.receiving", this.player.getName()
        ), TextFormatting.GOLD);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        CellphoneSessionsManager.deacceptPlayer(this.toPlayer, this.player, false);
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);

        String msg = StringUtils.translate("chat.cellphone.cancel.player", canceledBy);
        ServerUtils.sendChatToPlayer(this.player, msg, TextFormatting.RED);
        ServerUtils.sendChatToPlayer(this.toPlayer, msg, TextFormatting.RED);
    }

}
