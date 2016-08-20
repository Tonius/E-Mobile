package tonius.emobile.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientConfigTickHandler {
    
    private static Minecraft mc = Minecraft.getMinecraft();
    private static boolean configNeedsReset = false;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            if (mc.currentScreen instanceof GuiMainMenu && configNeedsReset) {
                configNeedsReset = false;
                EMConfig.onConfigChanged("emobile");
            } else if (mc.inGameHasFocus) {
                configNeedsReset = true;
            }
        }
    }
    
}
