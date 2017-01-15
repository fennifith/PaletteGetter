package james.palettegetter;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class ColorUtils {

    public static boolean isColorDark(int color) {
        return (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255 < 0.5;
    }

    @ColorInt
    public static int darkColor(@ColorInt int color) {
        return Color.argb(255, addToColorPart(Color.red(color), -30), addToColorPart(Color.green(color), -30), addToColorPart(Color.blue(color), -30));
    }

    @ColorInt
    public static int lightColor(@ColorInt int color) {
        return Color.argb(255, addToColorPart(Color.red(color), 30), addToColorPart(Color.green(color), 30), addToColorPart(Color.blue(color), 30));
    }

    static int addToColorPart(int colorPart, int variable) {
        return Math.max(0, Math.min(255, colorPart + variable));
    }

}
