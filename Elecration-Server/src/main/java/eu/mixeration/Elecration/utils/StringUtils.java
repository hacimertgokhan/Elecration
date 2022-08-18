package eu.mixeration.Elecration.utils;

import org.bukkit.ChatColor;

import java.awt.*;

public class StringUtils {

    public static String doColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Converts a hex string to a color. If it can't be converted null is returned.
     *
     * @param hex (i.e. #CCCCCCFF or CCCCCC)
     * @return Color
     */
    public static Color HexToColor(String hex) {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
        }
        return null;
    }

}
