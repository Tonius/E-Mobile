package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CellphoneSessionBase {
    
    protected int countdownSecs = -1;
    protected int ticks;
    protected int duration;
    protected boolean isValid = true;
    
    public CellphoneSessionBase(int duration) {
        this.duration = duration;
        CellphoneSessionsHandler.addSession(this);
        this.startCountdown();
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
    
    public abstract void onCountdownSecond();
    
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
    
    public abstract boolean isPlayerInSession(EntityPlayer player);
    
}
