package tonius.emobile.util;

import net.minecraft.client.resources.I18n;
import tonius.emobile.EMobile;

import java.text.DecimalFormat;

public class StringUtils {

    private static final DecimalFormat FORMAT = new DecimalFormat("###,###");

    public static String getFormattedNumber(int number) {
        return FORMAT.format(number);
    }

    public static String translate(String unlocalized, Object... parameters) {
        return translate(unlocalized, true, parameters);
    }

    public static String translate(String unlocalized, boolean prefix, Object... parameters) {
        if (prefix) {
            return I18n.format(EMobile.PREFIX + unlocalized, parameters);
        }
        return I18n.format(unlocalized, parameters);
    }

}
