package james.palettegetter;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class PaletteGetter {

    @Nullable
    @ColorInt
    public static Integer getColor(Context context, ComponentName componentName) {
        PackageManager packageManager = context.getPackageManager();

        ActivityInfo activityInfo = null;
        PackageInfo packageInfo = null;
        Resources resources = null, activityResources = null;
        try {
            packageInfo = packageManager.getPackageInfo(componentName.getPackageName(), PackageManager.GET_META_DATA);
            resources = packageManager.getResourcesForApplication(packageInfo.applicationInfo);
            activityInfo = packageManager.getActivityInfo(componentName, 0);
            activityResources = packageManager.getResourcesForActivity(componentName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        if (packageInfo != null && resources != null) {
            if (activityInfo != null && activityResources != null) {
                List<Integer> activityStatusBarColors = getResourceColors(activityInfo.packageName, resources, activityInfo.theme);
                if (activityStatusBarColors.size() > 0) {
                    return activityStatusBarColors.get(0);
                }
            }

            List<Integer> statusBarColors = getResourceColors(packageInfo.packageName, resources, packageInfo.applicationInfo.theme);
            if (statusBarColors.size() > 0) {
                return statusBarColors.get(0);
            }

            if (packageInfo.activities != null) {
                for (ActivityInfo otherActivityInfo : packageInfo.activities) {
                    List<Integer> otherStatusBarColors = getResourceColors(packageInfo.packageName, resources, otherActivityInfo.theme);
                    if (otherStatusBarColors.size() > 0) {
                        return otherStatusBarColors.get(0);
                    }
                }
            }
        }

        return null;
    }

    public static List<Integer> getColors(Context context, String packageName) {
        List<Integer> colors = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();

        PackageInfo packageInfo = null;
        Resources resources = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            resources = packageManager.getResourcesForApplication(packageInfo.applicationInfo);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        if (packageInfo != null && resources != null) {
            colors.addAll(getResourceColors(packageInfo.packageName, resources, packageInfo.applicationInfo.theme));

            if (packageInfo.activities != null) {
                for (ActivityInfo activityInfo : packageInfo.activities) {
                    Resources activityResources = null;

                    try {
                        activityResources = packageManager.getResourcesForActivity(new ComponentName(activityInfo.packageName, activityInfo.name));
                    } catch (PackageManager.NameNotFoundException ignored) {
                    }

                    if (activityResources != null) {
                        colors.addAll(getResourceColors(activityInfo.packageName, resources, activityInfo.theme));
                    }

                    colors.addAll(getResourceColors(packageInfo.packageName, resources, packageInfo.applicationInfo.theme));

                    if (packageInfo.activities != null) {
                        for (ActivityInfo otherActivityInfo : packageInfo.activities) {
                            colors.addAll(getResourceColors(packageInfo.packageName, resources, otherActivityInfo.theme));
                        }
                    }
                }
            }
        }

        return colors;
    }

    private static List<Integer> getResourceColors(String packageName, Resources resources, int style) {
        List<Integer> colors = new ArrayList<>();

        Resources.Theme theme = resources.newTheme();
        theme.applyStyle(style, true);

        TypedArray typedArray = theme.obtainStyledAttributes(style, new int[]{
                resources.getIdentifier("colorPrimaryDark", "attr", packageName),
                resources.getIdentifier("statusBarColor", "attr", packageName),
                resources.getIdentifier("colorPrimaryDark", "color", packageName)
        });

        for (int i = 0; i < typedArray.length(); i++) {
            int statusBarRes = typedArray.getResourceId(i, 0);
            if (statusBarRes != 0) {
                try {
                    colors.add(lightColor(ResourcesCompat.getColor(resources, statusBarRes, theme)));
                } catch (Resources.NotFoundException ignored) {
                }
            }
        }

        typedArray = theme.obtainStyledAttributes(style, new int[]{
                resources.getIdentifier("colorPrimary", "attr", packageName),
                resources.getIdentifier("colorPrimary", "color", packageName),
                resources.getIdentifier("navigationBarColor", "attr", packageName),
                resources.getIdentifier("colorAccent", "color", packageName)
        });

        for (int i = 0; i < typedArray.length(); i++) {
            int statusBarRes = typedArray.getResourceId(i, 0);
            if (statusBarRes != 0) {
                try {
                    colors.add(ResourcesCompat.getColor(resources, statusBarRes, theme));
                } catch (Resources.NotFoundException ignored) {
                }
            }
        }

        return colors;
    }

    @ColorInt
    private static int darkColor(@ColorInt int color) {
        return Color.argb(255, addToColorPart(Color.red(color), -70), addToColorPart(Color.green(color), -70), addToColorPart(Color.blue(color), -70));
    }

    @ColorInt
    private static int lightColor(@ColorInt int color) {
        return Color.argb(255, addToColorPart(Color.red(color), 70), addToColorPart(Color.green(color), 70), addToColorPart(Color.blue(color), 70));
    }

    private static int addToColorPart(int colorPart, int variable) {
        return Math.max(0, Math.min(255, colorPart + variable));
    }

}
