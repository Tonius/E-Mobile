package tonius.emobile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.Logger;

import tonius.emobile.config.ClientConfigTickHandler;
import tonius.emobile.config.EMConfig;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.network.message.MessageConfigSync;
import tonius.emobile.session.CellphoneSessionsHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "emobile", guiFactory = "tonius.emobile.config.ConfigGuiFactoryEM")
public class EMobile {
    
    @Instance("emobile")
    public static EMobile instance;
    public static Logger logger;
    
    public static Item cellphone = null;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.info("Starting E-Mobile");
        
        EMConfig.preInit(evt);
        
        if (EMConfig.enderPearlStackSize != EMConfig.enderPearlStackSize_default) {
            logger.info("Setting new Ender Pearl stack size");
            Items.ender_pearl.setMaxStackSize(EMConfig.enderPearlStackSize);
        }
        
        logger.info("Registering items");
        cellphone = new ItemCellphone();
        GameRegistry.registerItem(cellphone, "cellphone");
        
        logger.info("Registering handlers");
        PacketHandler.preInit();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new EMGuiHandler());
        FMLCommonHandler.instance().bus().register(instance);
        FMLCommonHandler.instance().bus().register(new CellphoneSessionsHandler());
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(new ClientConfigTickHandler());
        }
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        logger.info("Registering recipes");
        GameRegistry.addRecipe(new ShapedOreRecipe(cellphone, new Object[] { " IS", "IPI", "III", 'S', "stickWood", 'I', "ingotIron", 'P', Items.ender_pearl }));
    }
    
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent evt) {
        PacketHandler.instance.sendTo(new MessageConfigSync(), (EntityPlayerMP) evt.player);
    }
    
}
