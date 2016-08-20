package tonius.emobile.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import tonius.emobile.config.EMConfig.ConfigSection;
import tonius.emobile.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigGuiEM extends GuiConfig {
    
    public ConfigGuiEM(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), "emobile", false, false, StringUtils.translate("config.title"));
    }
    
    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = "emobile.config.";
        
        for (ConfigSection configSection : EMConfig.configSections) {
            list.add(new ConfigElement<ConfigCategory>(EMConfig.config.getCategory(configSection.toLowerCase()).setLanguageKey(prefix + configSection.id)));
        }
        
        return list;
    }
    
}
