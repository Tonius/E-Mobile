package tonius.emobile.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import tonius.emobile.CommonProxy;
import tonius.emobile.client.audio.SoundDialling;
import tonius.emobile.config.ClientConfigTickHandler;

import java.util.Random;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static Random rand = new Random();

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new ClientConfigTickHandler());
    }

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

            mc.theWorld.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, velX, velY, velZ);
        }
    }

    @Override
    public void showTeleportParticles(double posX, double posY, double posZ) {
        for (int i = 0; i <= 150; i++) {
            double velX = -0.2D * rand.nextGaussian();
            double velY = -0.2D * rand.nextGaussian();
            double velZ = -0.2D * rand.nextGaussian();

            mc.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, velX, velY, velZ);
        }
    }

}
