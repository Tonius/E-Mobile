package tonius.emobile.common.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import tonius.emobile.common.EMobile;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EMConfig {
    
    public static Configuration config;
    public static List<ConfigSection> configSections = new ArrayList<ConfigSection>();
    
    public static final ConfigSection sectionGeneral = new ConfigSection("General Settings", "general");
    public static final ConfigSection sectionTweaks = new ConfigSection("Tweaks Settings", "tweaks");
    public static final ConfigSection sectionFluxCellphone = new ConfigSection("Flux Cellphone Settings", "fluxCellphone");
    
    // general default
    public static final boolean allowTeleportPlayers_default = true;
    public static final boolean allowTeleportHome_default = true;
    public static final boolean allowTeleportSpawn_default = true;
    public static final int[] dimensionsBlacklist_default = new int[0];
    public static final boolean dimensionsWhitelist_default = false;
    
    // tweaks default
    public static final int enderPearlStackSize_default = 16;
    
    // fluxCellphone default
    public static final int fluxCellphoneMaxEnergy_default = 100000;
    public static final int fluxCellphoneMaxInput_default = 200;
    public static final int fluxCellphoneEnergyPerUse_default = 12800;
    
    // item
    public static boolean allowTeleportPlayers = allowTeleportHome_default;
    public static boolean allowTeleportHome = allowTeleportHome_default;
    public static boolean allowTeleportSpawn = allowTeleportHome_default;
    public static int[] dimensionsBlacklist = dimensionsBlacklist_default;
    public static boolean dimensionsWhitelist = dimensionsWhitelist_default;
    
    // tweaks
    public static int enderPearlStackSize = enderPearlStackSize_default;
    
    // fluxCellphone
    public static int fluxCellphoneMaxEnergy = fluxCellphoneMaxEnergy_default;
    public static int fluxCellphoneMaxInput = fluxCellphoneMaxInput_default;
    public static int fluxCellphoneEnergyPerUse = fluxCellphoneEnergyPerUse_default;
    
    public static void preInit(FMLPreInitializationEvent evt) {
        FMLCommonHandler.instance().bus().register(new EMConfig());
        config = new Configuration(evt.getSuggestedConfigurationFile());
        EMobile.logger.info("Loading configuration file");
        syncConfig();
    }
    
    public static void syncConfig() {
        try {
            processConfig();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            config.save();
        }
        if (EMobile.cellphoneRF != null) {
            EMobile.cellphoneRF.maxEnergy = fluxCellphoneMaxEnergy;
            EMobile.cellphoneRF.maxInput = fluxCellphoneMaxInput;
            EMobile.cellphoneRF.energyPerUse = fluxCellphoneEnergyPerUse;
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent evt) {
        onConfigChanged(evt.modID);
    }
    
    public static void onConfigChanged(String modID) {
        if (modID.equals("emobile")) {
            EMobile.logger.info("Updating configuration file");
            syncConfig();
        }
    }
    
    public static void processConfig() {
        allowTeleportPlayers = config.get(sectionGeneral.name, "Allow teleporting to players", allowTeleportPlayers_default, "When enabled, the Ender Cellphone may be used to teleport to other players.").getBoolean(allowTeleportPlayers_default);
        allowTeleportHome = config.get(sectionGeneral.name, "Allow teleporting home", allowTeleportHome_default, "When enabled, the Ender Cellphone may be used by players to teleport to their beds.").getBoolean(allowTeleportHome_default);
        allowTeleportSpawn = config.get(sectionGeneral.name, "Allow teleporting to spawn", allowTeleportSpawn_default, "When enabled, the Ender Cellphone may be used to teleport to the world spawn.").getBoolean(allowTeleportSpawn_default);
        dimensionsBlacklist = config.get(sectionGeneral.name, "Dimensions Blacklist", dimensionsBlacklist_default, "The blacklist of dimension ids that can be teleported to or from using the Ender Cellphone. These dimensions may not be teleported to or from.").getIntList();
        dimensionsWhitelist = config.get(sectionGeneral.name, "Dimensions Whitelist", dimensionsWhitelist_default, "If enabled, the blacklist of dimension ids will be treated as a whitelist instead. The dimensions will then be the only dimensions that may be teleported to or from.").getBoolean(dimensionsWhitelist_default);
        
        enderPearlStackSize = config.get(sectionTweaks.name, "Ender Pearl stack size", enderPearlStackSize_default, "This config option can be used to change the maximum stack size of Ender Pearls.").setMinValue(1).setMaxValue(512).setRequiresMcRestart(true).getInt(enderPearlStackSize_default);
        
        fluxCellphoneMaxEnergy = config.get(sectionFluxCellphone.name, "Max Energy", fluxCellphoneMaxEnergy_default, "The maximum amount of RF that a Flux Cellphone can store.").setMinValue(1).getInt(fluxCellphoneMaxEnergy_default);
        fluxCellphoneMaxInput = config.get(sectionFluxCellphone.name, "Max Input", fluxCellphoneMaxInput_default, "The maximum RF/t rate that the Flux Cellphone can be charged with.").setMinValue(0).getInt(fluxCellphoneMaxInput_default);
        fluxCellphoneEnergyPerUse = config.get(sectionFluxCellphone.name, "Energy Per Use", fluxCellphoneEnergyPerUse_default, "The amount of RF that the Flux Cellphone consumes when teleporting.").setMinValue(0).getInt(fluxCellphoneEnergyPerUse_default);
    }
    
    public static class ConfigSection {
        
        public String name;
        public String id;
        
        public ConfigSection(String name, String id) {
            this.name = name;
            this.id = id;
            EMConfig.configSections.add(this);
        }
        
        public String toLowerCase() {
            return this.name.toLowerCase();
        }
        
    }
    
}
