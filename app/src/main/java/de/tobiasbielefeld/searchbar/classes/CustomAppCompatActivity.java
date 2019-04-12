/*
 * Copyright (C) 2017  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package de.tobiasbielefeld.searchbar.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.helper.LocaleChanger;

import static de.tobiasbielefeld.searchbar.SharedData.*;

/**
 * Custom AppCompatActivity to implement language changing in attachBaseContext()
 * and some settings in onResume().  It also sets the Preferences, in case the app
 * was paused for a longer time and the references got lost.
 */

@SuppressLint("Registered")
public class CustomAppCompatActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        reinitializeData(this);
        applyTheme();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleChanger.onAttach(base));
    }

    /**
     * Apply the preferences from orientation and status bar to the current activity. It will be
     * called in the onResume, so after changing the preferences I don't need a listener to update
     * the changes on the previous activities.
     */
    @Override
    public void onResume() {
        super.onResume();
        setOrientation(this);
        showOrHideStatusBar(this);
    }

    /**
     * Sets the screen orientation according to the settings. It is called from onResume().
     * @param activity The activity to apply the orientation on.
     */
    public static void setOrientation(Activity activity) {
        switch (getSavedString(PREF_ORIENTATION, "1")) {
            case "1": //follow system settings
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                break;
            case "2": //portrait
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3": //landscape
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case "4": //landscape upside down
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
        }
    }

    /**
     * Hides the status bar according to the settings. It is called from onResume().
     * @param activity The activity to apply the changes on.
     */
    public void showOrHideStatusBar(Activity activity) {
        if (getSavedBoolean(PREF_STATUS_BAR, false))
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void applyTheme() {
        switch (getSavedString(PREF_THEME, DEFAULT_THEME)) {
            case "light":
                setTheme(R.style.AppTheme);
                break;
            case "dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            case "black":
                setTheme(R.style.AppTheme_Black);
                break;
        }
    }
}
