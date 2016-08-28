package tonius.emobile.client.audio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import tonius.emobile.EMobile;

public class SoundDialling extends MovingSound {

    private EntityPlayer user;

    public SoundDialling(EntityPlayer user) {
        super(EMobile.phoneCountdownSound, SoundCategory.PLAYERS);
        this.user = user;
    }

    @Override
    public void update() {
        this.xPosF = (float) this.user.posX;
        this.yPosF = (float) this.user.posY;
        this.zPosF = (float) this.user.posZ;
    }

}
