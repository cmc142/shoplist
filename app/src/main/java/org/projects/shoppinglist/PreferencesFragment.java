package org.projects.shoppinglist;

/**
 * Created by user on 04-06-2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PreferencesFragment extends PreferenceFragment {


    private static String uname = "name";

    public static String UName(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(uname, "");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding the preferences from the xml
        //so this will in fact be the whole view.
        addPreferencesFromResource(R.xml.prefs);
    }
}
