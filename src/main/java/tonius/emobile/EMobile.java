package tonius.emobile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;
import tonius.emobile.config.ClientConfigTickHandler;
import tonius.emobile.config.EMConfig;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.item.ItemCellphoneRF;
import tonius.emobile.network.PacketHandler;
import tonius.emobile.network.message.MessageConfigSync;
import tonius.emobile.session.CellphoneSessionsHandler;

@Mod(modid = "emobile", guiFactory = "tonius.emobile.config.ConfigGuiFactoryEM", dependencies = "after:ThermalExpansion")
public class EMobile {
    
    @Mod.Instance("emobile")
    public static EMobile instance;
    @SidedProxy(serverSide = "tonius.emobile.CommonProxy", clientSide = "tonius.emobile.client.ClientProxy")
    public static CommonProxy proxy;
    public static Logger logger;
    
    public static ItemCellphone cellphone = null;
    public static ItemCellphoneRF cellphoneRF = null;
    
    @Mod.EventHandler
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
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        logger.info("Registering recipes");
        if (Loader.isModLoaded("ThermalExpansion")) {
            GameRegistry.addRecipe(new ShapedOreRecipe(cellphone, new Object[] { " IS", "IPI", "III", 'S', "nuggetCopper", 'I', "ingotInvar", 'P', GameRegistry.findItemStack("ThermalFoundation", "bucketEnder", 1) }));
            if (cellphoneRF != null) {
                GameRegistry.addRecipe(new ShapedOreRecipe(cellphoneRF, new Object[] { " IS", "IPI", "III", 'S', GameRegistry.findItemStack("ThermalExpansion", "powerCoilGold", 1), 'I', "ingotSignalum", 'P', cellphone }));
            }
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(cellphone, new Object[] { " IS", "IPI", "III", 'S', "stickWood", 'I', "ingotIron", 'P', Items.ender_pearl }));
            if (cellphoneRF != null) {
                GameRegistry.addRecipe(new ShapedOreRecipe(cellphoneRF, new Object[] { " IS", "IPI", "RIR", 'S', "gemDiamond", 'I', "ingotGold", 'P', cellphone, 'R', "dustRedstone" }));
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        PacketHandler.instance.sendTo(new MessageConfigSync(), (EntityPlayerMP) evt.player);
    }
    
}
