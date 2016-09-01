package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import tonius.emobile.util.StringUtils;

public abstract class CellphoneSessionBase {

    protected EntityPlayerMP player;
    protected int countdownSecs = -1;
    protected int ticks;
    protected final int duration = 8;
    protected boolean isValid = true;

    public CellphoneSessionBase(EntityPlayerMP player) {
        this.player = player;
    }

    public boolean isPlayerInSession(EntityPlayer player) {
        return this.player.equals(player);
    }

    public void tick() {
        if (this.isValid) {
            if (this.countdownSecs >= 0) {
                this.ticks++;

                if (this.ticks % 20 == 0) {
                    this.countdownSecs--;
                    if (this.countdownSecs > 0) {
                        this.onCountdownSecond();
                    }
                }

                if (this.countdownSecs <= 0) {
                    this.onCountdownFinished();
                    this.invalidate();
                }
            }
        }
    }

    public void startCountdown() {
        this.countdownSecs = this.duration + 1;
    }

    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            this.player.addChatMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE +
                    StringUtils.translate("chat.cellphone.countdown", this.countdownSecs)));
        }
    }

    public abstract void onCountdownFinished();

    public boolean isValid() {
        return this.isValid;
    }

    public void invalidate() {
        this.isValid = false;
    }

    public void cancel(String canceledBy) {
        this.invalidate();
    }

}
