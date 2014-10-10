package tonius.emobile.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import tonius.emobile.common.config.EMConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ClientConfigTickHandler {
    
    private static Minecraft mc = Minecraft.getMinecraft();
    private static boolean configNeedsReset = false;
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent evt) {
        if (evt.phase == Phase.END) {
            if (mc.currentScreen instanceof GuiMainMenu && configNeedsReset) {
                configNeedsReset = false;
                EMConfig.onConfigChanged("emobile");
            } else if (mc.inGameHasFocus) {
                configNeedsReset = true;
            }
        }
    }
    
}
