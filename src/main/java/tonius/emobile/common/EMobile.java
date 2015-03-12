package tonius.emobile.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.Logger;

import tonius.emobile.client.config.ClientConfigTickHandler;
import tonius.emobile.common.config.EMConfig;
import tonius.emobile.common.gui.EMGuiHandler;
import tonius.emobile.common.item.ItemCellphone;
import tonius.emobile.common.item.ItemCellphoneRF;
import tonius.emobile.common.network.PacketHandler;
import tonius.emobile.common.network.message.MessageConfigSync;
import tonius.emobile.common.session.CellphoneSessionsHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "emobile", guiFactory = "tonius.emobile.client.config.ConfigGuiFactoryEM", dependencies = "after:ThermalExpansion")
public class EMobile {
    
    @Instance("emobile")
    public static EMobile instance;
    @SidedProxy(serverSide = "tonius.emobile.common.CommonProxy", clientSide = "tonius.emobile.client.ClientProxy")
    public static CommonProxy proxy;
    public static Logger logger;
    
    public static ItemCellphone cellphone = null;
    public static ItemCellphoneRF cellphoneRF = null;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.info("Starting E-Mobile");
        
        EMConfig.preInit(evt);
        
        if (EMConfig.enderPearlStackSize != Items.ender_pearl.getItemStackLimit()) {
            logger.info("Setting new Ender Pearl stack size");
            Items.ender_pearl.setMaxStackSize(EMConfig.enderPearlStackSize);
        }
        
        logger.info("Registering items");
        cellphone = new ItemCellphone();
        GameRegistry.registerItem(cellphone, "cellphone");
        if (EMConfig.fluxCellphoneEnabled) {
            cellphoneRF = new ItemCellphoneRF(EMConfig.fluxCellphoneMaxEnergy, EMConfig.fluxCellphoneMaxInput, EMConfig.fluxCellphoneEnergyPerUse);
            GameRegistry.registerItem(cellphoneRF, "cellphoneRF");
        }
        
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
        if (cellphoneRF != null) {
            if (Loader.isModLoaded("ThermalExpansion")) {
                
            } else {
                GameRegistry.addRecipe(new ShapedOreRecipe(cellphone, new Object[] { " IS", "IPI", "IRI", 'S', "stickWood", 'I', "ingotIron", 'P', Items.ender_pearl, 'R', "blockRedstone" }));
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent evt) {
        PacketHandler.instance.sendTo(new MessageConfigSync(), (EntityPlayerMP) evt.player);
    }
    
}
