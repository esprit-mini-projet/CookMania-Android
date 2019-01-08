package tn.duoes.esprit.cookmania.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Stack;

public class NavigationUtils {

    private static final String TAG = NavigationUtils.class.getSimpleName();
    public static final String NAVIGATION_KEY = "goTo";
    public static Stack<Integer> pagesStack = new Stack<>();

    public static Intent getNavigationFormattedIntent(Context context, Class destinationClass){
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(NAVIGATION_KEY, destinationClass.getCanonicalName());
        return intent;
    }

    public static Intent getParentActivityIntent(Context context, Intent fromIntent){
        String className = fromIntent.getExtras().getString(NAVIGATION_KEY);
        try {
            Intent intent = new Intent(context, Class.forName(className));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            return intent;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "getParentActivityIntent: ", e);
        }
        return null;
    }

}
