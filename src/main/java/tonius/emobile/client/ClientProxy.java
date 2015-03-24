package tonius.emobile.client;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import tonius.emobile.CommonProxy;
import tonius.emobile.client.audio.SoundDialling;

public class ClientProxy extends CommonProxy {
    
    private static Minecraft mc = Minecraft.getMinecraft();
    private static Random rand = new Random();
    
    @Override
    public void playDiallingSound(EntityPlayer player) {
        mc.getSoundHandler().playSound(new SoundDialling(player));
    }
    
    @Override
    public void showDiallingParticles(double posX, double posY, double posZ) {
        for (int i = 0; i <= 15; i++) {
            double velX = rand.nextGaussian();
            double velY = rand.nextGaussian();
            double velZ = rand.nextGaussian();
            mc.theWorld.spawnParticle("portal", posX, posY, posZ, velX, velY, velZ);
        }
    }
    
    @Override
    public void showTeleportParticles(double posX, double posY, double posZ) {
        for (int i = 0; i <= 150; i++) {
            double velX = -rand.nextGaussian() * 0.2D;
            double velY = -rand.nextGaussian() * 0.2D;
            double velZ = -rand.nextGaussian() * 0.2D;
            mc.theWorld.spawnParticle("explode", posX, posY, posZ, velX, velY, velZ);
        }
    }
    
}
