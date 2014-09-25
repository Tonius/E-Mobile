package tonius.emobile.audio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class SoundDialling extends MovingSound {
    
    private EntityPlayer user;
    
    public SoundDialling(EntityPlayer user) {
        super(new ResourceLocation("emobile:phonecountdown"));
        this.user = user;
    }
    
    @Override
    public void update() {
        this.xPosF = (float) this.user.posX;
        this.yPosF = (float) this.user.posY;
        this.zPosF = (float) this.user.posZ;
    }
    
}
