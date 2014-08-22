package tonius.emobile.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import tonius.emobile.EMobile;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EMConfig {

    public static Configuration config;
    public static List<ConfigSection> configSections = new ArrayList<ConfigSection>();

    public static final ConfigSection sectionGeneral = new ConfigSection("General Settings", "general");
    public static final ConfigSection sectionTweaks = new ConfigSection("Tweaks Settings", "tweaks");

    // general default
    public static final boolean allowTeleportPlayers_default = true;
    public static final boolean allowTeleportHome_default = true;
    public static final boolean allowTeleportSpawn_default = true;
    public static final int[] dimensionsBlacklist_default = new int[0];
    public static final boolean dimensionsWhitelist_default = false;

    // tweaks default
    public static final int enderPearlStackSize_default = 16;

    // item
    public static boolean allowTeleportPlayers = allowTeleportHome_default;
    public static boolean allowTeleportHome = allowTeleportHome_default;
    public static boolean allowTeleportSpawn = allowTeleportHome_default;
    public static int[] dimensionsBlacklist = dimensionsBlacklist_default;
    public static boolean dimensionsWhitelist = dimensionsWhitelist_default;

    // tweaks
    public static int enderPearlStackSize = enderPearlStackSize_default;

    public static void preInit(FMLPreInitializationEvent evt) {
        FMLCommonHandler.instance().bus().register(new EMConfig());
        config = new Configuration(evt.getSuggestedConfigurationFile());
        EMobile.log.info("Loading configuration file");
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
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent evt) {
        if (evt.modID.equals("emobile")) {
            EMobile.log.info("Updating configuration file");
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
    }

}
