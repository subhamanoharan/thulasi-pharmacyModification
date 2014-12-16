package com.cryptic.imed.util.view;

import android.app.Activity;
import android.view.View;

/**
 * @author sharafat
 */
public class DualPaneUtils {
    public static boolean isDualPane(Activity activity, int viewIdToLookForInActivity) {
        View view = activity.findViewById(viewIdToLookForInActivity);
        return view != null && view.getVisibility() == View.VISIBLE;
    }
}
