package tonius.emobile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.session.CellphoneSessionsManager;

public class CommonProxy {

    public void registerHandlers() {
        PacketHandler.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new EMGuiHandler());

        MinecraftForge.EVENT_BUS.register(EMobile.instance);
        MinecraftForge.EVENT_BUS.register(new CellphoneSessionsManager());
    }

    public void playDiallingSound(EntityPlayer player) {
    }

    public void showDiallingParticles(double posX, double posY, double posZ) {
    }

    public void showTeleportParticles(double posX, double posY, double posZ) {
    }

}
