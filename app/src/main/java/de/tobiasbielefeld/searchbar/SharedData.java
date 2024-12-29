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

package de.tobiasbielefeld.searchbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import de.tobiasbielefeld.searchbar.helper.Records;

/**
 * Contains some static stuff
 */

public class SharedData {

    public static SharedPreferences sharedPref;

    public static String PREF_RECORD_LIST_SIZE;
    public static String PREF_RECORD_ENTRY;
    public static String PREF_THEME;
    public static String PREF_HIDE_APP_ICON;
    public static String PREF_SEARCH_URL;
    public static String PREF_SEARCH_SELECTED_INDEX;
    public static String PREF_CUSTOM_SEARCH_URL;
    public static String PREF_ORIENTATION;
    public static String PREF_STATUS_BAR;
    public static String PREF_LANGUAGE;
    public static String PREF_CLOSE_AFTER_SEARCH;
    public static String PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE;

    public static String DEFAULT_SEARCH_URL;
    public static String DEFAULT_ORIENTATION;
    public static boolean DEFAULT_STATUS_BAR;
    public static boolean DEFAULT_CLOSE_AFTER_SEARCH;
    public static boolean DEFAULT_EDGE_TO_EDGE_DISPLAY_MODE;
    public static int DEFAULT_THEME;
    public static int DEFAULT_SEARCH_SELECTED_INDEX;
    public static boolean DEFAULT_HIDE_APP_ICON;

    public static Records records;

    private static Toast toast;

    public static void reinitializeData(Context context){

        Resources res = context.getResources();

        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if (PREF_RECORD_ENTRY == null) {
            PREF_RECORD_LIST_SIZE = "record_list_size";
            PREF_RECORD_ENTRY = "record_entry_";
            PREF_SEARCH_URL = "search_url";
            PREF_SEARCH_SELECTED_INDEX = res.getString(R.string.pref_key_search_engine);
            PREF_ORIENTATION = res.getString(R.string.pref_key_orientation);
            PREF_STATUS_BAR = res.getString(R.string.pref_key_hide_status_bar);
            PREF_LANGUAGE = res.getString(R.string.pref_key_language);
            PREF_THEME = res.getString(R.string.pref_key_theme);
            PREF_CUSTOM_SEARCH_URL = res.getString(R.string.pref_key_custom_search_url);
            PREF_HIDE_APP_ICON = res.getString(R.string.pref_key_hide_app_icon);
            PREF_CLOSE_AFTER_SEARCH = res.getString(R.string.pref_key_close_after_search);
            PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE = res.getString(R.string.pref_key_edge_to_edge_display_mode);

            DEFAULT_SEARCH_SELECTED_INDEX = res.getInteger(R.integer.default_search_engine_v2);
            DEFAULT_SEARCH_URL = res.getStringArray(R.array.search_engine_uris)[DEFAULT_SEARCH_SELECTED_INDEX];
            DEFAULT_ORIENTATION = res.getStringArray(R.array.pref_orientation_values)[0];
            DEFAULT_STATUS_BAR = res.getBoolean(R.bool.default_status_bar);
            DEFAULT_THEME = res.getInteger(R.integer.default_theme);
            DEFAULT_HIDE_APP_ICON = res.getBoolean(R.bool.default_hide_app_icon);
            DEFAULT_CLOSE_AFTER_SEARCH = res.getBoolean(R.bool.default_close_after_search);
            DEFAULT_EDGE_TO_EDGE_DISPLAY_MODE = res.getBoolean(R.bool.default_edge_to_edge_display_mode);
        }
    }


    public static int getSavedInt(String name, int defaultValue) {
        return sharedPref.getInt(name, defaultValue);
    }

    public static boolean getSavedBoolean(String name, boolean defaultValue) {
        return sharedPref.getBoolean(name, defaultValue);
    }

    public static String getSavedString(String name, String defaultValue) {
        return sharedPref.getString(name, defaultValue);
    }

    public static void putSavedInt(String name, int value) {
        sharedPref.edit().putInt(name, value).apply();
    }

    public static void putSavedBoolean(String name, boolean value) {
        sharedPref.edit().putBoolean(name, value).apply();
    }

    public static void putSavedString(String name, String value) {
        sharedPref.edit().putString(name, value).apply();
    }

    public static int getSavedTheme() {
        return Integer.parseInt(getSavedString(PREF_THEME, "0"));
    }

    public static int getSavedOrientation() {
        return Integer.parseInt(getSavedString(PREF_ORIENTATION, "1"));
    }

    public static boolean getSavedCloseAfterSearch() {
        return getSavedBoolean(PREF_CLOSE_AFTER_SEARCH, DEFAULT_CLOSE_AFTER_SEARCH);
    }

    public static boolean getSavedEdgeToEdgeDisplayMode() {
        return getSavedBoolean(PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE, DEFAULT_EDGE_TO_EDGE_DISPLAY_MODE);
    }

    public static void logText(String text){
        Log.e("hey",text);
    }

    /**
     * Uses the given string array to create a text paragraph. The strings are separated by bullet
     * characters.
     *
     * @param strings The string array to use for the text paragraph
     * @return a charSequence, which can directly be applied to a textView
     */
    static public CharSequence createBulletParagraph(CharSequence[] strings){

        SpannableString[] spans = new SpannableString[strings.length];

        //apply the bullet characters
        for (int i = 0; i < strings.length; i++){
            spans[i] = new SpannableString(strings[i] + (i < strings.length - 1 ? "\n" : ""));
            spans[i].setSpan(new BulletSpan(15), 0, strings[i].length(), 0);
        }

        //set up the textView
        return TextUtils.concat(spans);
    }

    /**
     * Shows the given text as a toast. New texts override the old one.
     *
     * @param text The text to show
     */
    @SuppressLint("ShowToast")
    public static void showToast(String text, Context context) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else
            toast.setText(text);

        toast.show();
    }

    public static void showOrHideStatusBar(Window window, boolean shouldShow) {
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(window, window.getDecorView());

        if (shouldShow) {
            windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        } else {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
        }
    }

    /**
     * Finds the best match for a given system locale. First searches for an exact match, then for
     * a match in the main part (eg. return "de" when "de-DE" is target) and finally find a match
     * with a different region (eg. return "es-AR" when "es-US" is target)
     *
     * @param availableLocales locale strings available in this app
     * @param targetLocale the locale string as set by the system library
     * @return index of best match
     */
    static public int findBestLocaleMatchIndex(String[] availableLocales, String targetLocale) {
        // first search for exact match
        logText(targetLocale);
        for (int i = 0; i < availableLocales.length; i++) {
            if (availableLocales[i].equals(targetLocale)) {
                return i;
            }
        }

        // then search for match in main language (e.g. "de" for "de-DE")
        int dividerIndex = targetLocale.indexOf("-");
        String targetLanguage = dividerIndex == -1 ? targetLocale : targetLocale.substring(0, dividerIndex);

        for (int i = 0; i < availableLocales.length; i++) {
            if (availableLocales[i].equals(targetLanguage)) {
                return i;
            }
        }

        // finally search for match of main language with another language (e.g. "es-AR" for "es-US")
        for (int i = 0; i < availableLocales.length; i++) {
            String locale = availableLocales[i];
            dividerIndex = locale.indexOf("-");
            String lang = dividerIndex == -1 ? locale : locale.substring(0, dividerIndex);

            if (lang.equals(targetLanguage)) {
                return i;
            }
        }

        // default locale is to use system settings, represented at index 0
        return 0;
    }
}
