package tonius.emobile;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import org.apache.logging.log4j.Logger;

import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.session.CellphoneSessionsHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "emobile")
public class EMobile {

    @Instance("emobile")
    public static EMobile instance;
    public static Logger log;

    public static Item cellphone = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        log = evt.getModLog();
        log.info("Starting E-Mobile");

        log.info("Setting new Ender Pearl stack size");
        Items.ender_pearl.setMaxStackSize(64);

        log.info("Registering items");
        cellphone = new ItemCellphone();
        GameRegistry.registerItem(cellphone, "cellphone");

        log.info("Registering handlers");
        PacketHandler.preInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new EMGuiHandler());
        FMLCommonHandler.instance().bus().register(new CellphoneSessionsHandler());
    }

}
