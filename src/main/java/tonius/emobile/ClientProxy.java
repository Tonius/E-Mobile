package tonius.emobile;

import net.minecraft.entity.player.EntityPlayer;
import tonius.emobile.audio.SoundDialling;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void playDiallingSound(EntityPlayer player) {
        FMLClientHandler.instance().getClient().getSoundHandler().playSound(new SoundDialling(player));
    }
    
}
