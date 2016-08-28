package tonius.emobile.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import tonius.emobile.EMobile;
import tonius.emobile.config.EMConfig.ConfigSection;
import tonius.emobile.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigGuiEM extends GuiConfig {

    public ConfigGuiEM(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), EMobile.MODID, false, false, StringUtils.translate("config.title"));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        String prefix = EMobile.PREFIX + "config.";

        for (ConfigSection configSection : EMConfig.configSections) {
            list.add(new ConfigElement(EMConfig.config
                    .getCategory(configSection.getName().toLowerCase())
                    .setLanguageKey(prefix + configSection.getId())
            ));
        }

        return list;
    }

}
