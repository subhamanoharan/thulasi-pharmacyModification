package com.cryptic.imed.util.view;

import android.app.Activity;
import android.os.Build;
import android.view.MenuItem;

/**
 * @author sharafat
 */
public class CompatibilityUtils {
    public static void setHomeButtonEnabled(boolean homeButtonEnabled, Activity activity) {
        /*
        Beginning with Android 4.0 (API level 14), the icon must explicitly be enabled as an action item by calling
        setHomeButtonEnabled(true). In previous versions, the icon was enabled as an action item by default.
         */
        if (Build.VERSION.SDK_INT >= 14) {
            activity.getActionBar().setHomeButtonEnabled(true);
        }
    }

    public static void setShowMenuAsAction(MenuItem menuItem, int actionEnum) {
        /* Action Bar is available since Android 3.0 (API level 11) */
        if (Build.VERSION.SDK_INT >= 11) {
            menuItem.setShowAsAction(actionEnum);
        }
    }

    public static void setShowMenuAsAction(MenuItem menuItem) {
        setShowMenuAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }
}
