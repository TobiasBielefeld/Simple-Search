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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.helper.Database;
import de.tobiasbielefeld.searchbar.helper.Records;
import de.tobiasbielefeld.searchbar.models.SearchEngine;

/**
 * Contains some static stuff
 */

public class SharedData {

    public static Database database;
    public static SharedPreferences sharedPref;
    public static String PREF_RECORD_LIST_SIZE;
    public static String PREF_RECORD_ENTRY;
    public static String PREF_THEME;
    public static String PREF_HIDE_APP_ICON;
    public static String PREF_SEARCH_LABEL;
    public static String PREF_ORIENTATION;
    public static String PREF_STATUS_BAR;
    public static String PREF_LANGUAGE;
    public static String PREF_ENABLE_RECORDS;
    public static String PREF_CLOSE_AFTER_SEARCH;
    public static String PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE;
    public static String PREF_TOOLBAR_COLOR;
    public static String PREF_KEEP_SELECTED_SEARCH_ENGINE;
    public static String PREF_START_ON_QUICK_SEARCH_SELECT;
    public static int DEFAULT_TOOLBAR_COLOR;
    public static int DEFAULT_RECORD_LIST_SIZE;
    public static String DEFAULT_ORIENTATION;
    public static String DEFAULT_SEARCH_LABEL;
    public static boolean DEFAULT_KEEP_SELECTED_SEARCH_ENGINE;
    public static boolean DEFAULT_START_ON_QUICK_SEARCH_SELECT;
    public static boolean DEFAULT_STATUS_BAR;
    public static boolean DEFAULT_ENABLE_RECORDS;
    public static boolean DEFAULT_CLOSE_AFTER_SEARCH;
    public static boolean DEFAULT_EDGE_TO_EDGE_DISPLAY_MODE;
    public static int DEFAULT_THEME;
    public static boolean DEFAULT_HIDE_APP_ICON;
    public static Records records;

    private static Toast toast;

    public static void reinitializeData(Context context){
        Resources res = context.getResources();

        if (PREF_RECORD_ENTRY == null) {
            PREF_RECORD_LIST_SIZE = "record_list_size";
            PREF_RECORD_ENTRY = "record_entry_";
            PREF_SEARCH_LABEL = "search_label";
            PREF_ORIENTATION = res.getString(R.string.pref_key_orientation);
            PREF_STATUS_BAR = res.getString(R.string.pref_key_hide_status_bar);
            PREF_LANGUAGE = res.getString(R.string.pref_key_language);
            PREF_THEME = res.getString(R.string.pref_key_theme);
            PREF_ENABLE_RECORDS = res.getString(R.string.pref_key_enable_records);
            PREF_HIDE_APP_ICON = res.getString(R.string.pref_key_hide_app_icon);
            PREF_CLOSE_AFTER_SEARCH = res.getString(R.string.pref_key_close_after_search);
            PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE = res.getString(R.string.pref_key_edge_to_edge_display_mode);
            PREF_KEEP_SELECTED_SEARCH_ENGINE = res.getString(R.string.pref_key_keep_selected_search_engine);
            PREF_START_ON_QUICK_SEARCH_SELECT = res.getString(R.string.pref_key_start_on_quick_search_select);
            PREF_TOOLBAR_COLOR = res.getString(R.string.pref_key_toolbar_color);
            DEFAULT_SEARCH_LABEL = "DuckDuckGo";
            DEFAULT_TOOLBAR_COLOR = res.getColor(R.color.colorPrimary);
            DEFAULT_ENABLE_RECORDS =  res.getBoolean(R.bool.default_enable_records);
            DEFAULT_ORIENTATION = res.getStringArray(R.array.pref_orientation_values)[0];
            DEFAULT_STATUS_BAR = res.getBoolean(R.bool.default_status_bar);
            DEFAULT_THEME = res.getInteger(R.integer.default_theme);
            DEFAULT_HIDE_APP_ICON = res.getBoolean(R.bool.default_hide_app_icon);
            DEFAULT_RECORD_LIST_SIZE = res.getInteger(R.integer.default_record_list_size);
            DEFAULT_CLOSE_AFTER_SEARCH = res.getBoolean(R.bool.default_close_after_search);
            DEFAULT_EDGE_TO_EDGE_DISPLAY_MODE = res.getBoolean(R.bool.default_edge_to_edge_display_mode);
            DEFAULT_KEEP_SELECTED_SEARCH_ENGINE = res.getBoolean(R.bool.default_keep_selected_search_engine);
            DEFAULT_START_ON_QUICK_SEARCH_SELECT = res.getBoolean(R.bool.default_start_on_quick_search_select);
        }

        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        // needs to be after sharedPref init, since it is used for some legacy imports
        if (database == null) {
            database = new Database(context);
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

    public static int getSavedRecordListSize() {
        return getSavedInt(PREF_RECORD_LIST_SIZE, DEFAULT_RECORD_LIST_SIZE);
    }

    public static boolean getSavedStatusBar() {
        return getSavedBoolean(PREF_STATUS_BAR, DEFAULT_STATUS_BAR);
    }

    public static boolean getSavedCloseAfterSearch() {
        return getSavedBoolean(PREF_CLOSE_AFTER_SEARCH, DEFAULT_CLOSE_AFTER_SEARCH);
    }

    public static int getSavedToolbarColor() {
        return getSavedInt(PREF_TOOLBAR_COLOR, DEFAULT_TOOLBAR_COLOR);
    }

    public static boolean getSavedEdgeToEdgeDisplayMode() {
        return getSavedBoolean(PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE, DEFAULT_EDGE_TO_EDGE_DISPLAY_MODE);
    }

    public static String getSavedSearchEngineLabel() {
        return getSavedString(PREF_SEARCH_LABEL, DEFAULT_SEARCH_LABEL);
    }

    public static boolean getSavedKeepSelectedSearchEngine() {
        return getSavedBoolean(PREF_KEEP_SELECTED_SEARCH_ENGINE, DEFAULT_KEEP_SELECTED_SEARCH_ENGINE);
    }
    public static boolean getSavedEnableRecords() {
        return getSavedBoolean(PREF_ENABLE_RECORDS, DEFAULT_ENABLE_RECORDS);
    }

    public static boolean getSavedStartOnQuickSearchSelect() {
        return getSavedBoolean(PREF_START_ON_QUICK_SEARCH_SELECT, DEFAULT_START_ON_QUICK_SEARCH_SELECT);
    }

    public static void putSavedSearchEngineLabel(String key) {
        putSavedString(PREF_SEARCH_LABEL, key);
    }

    public static void putSavedToolbarColor(int value) {
        putSavedInt(PREF_TOOLBAR_COLOR, value);
    }

    public static void putSavedTheme(int value) {
        putSavedString(PREF_THEME, String.valueOf(value));
    }

    public static void putSavedRecordListSize(int value) {
        putSavedInt(PREF_RECORD_LIST_SIZE, value);
    }

    public static void putSavedOrientation(int value) {
        putSavedString(PREF_ORIENTATION, String.valueOf(value));
    }

    public static void putSavedEdgeToEdgeDisplayMode(boolean value) {
        putSavedBoolean(PREF_USE_EDGE_TO_EDGE_DISPLAY_MODE, value);
    }

    public static void putSavedKeepSelectedSearchEngine(boolean value) {
        putSavedBoolean(PREF_KEEP_SELECTED_SEARCH_ENGINE, value);
    }

    public static void putSavedStartOnQuickSearchSelect(boolean value) {
        putSavedBoolean(PREF_START_ON_QUICK_SEARCH_SELECT, value);
    }

    public static void putSavedStatusBar(boolean value) {
        putSavedBoolean(PREF_STATUS_BAR, value);
    }

    public static void putSavedCloseAfterSearch(boolean value) {
        putSavedBoolean(PREF_CLOSE_AFTER_SEARCH, value);
    }

    public static void putSavedEnableRecords(boolean value) {
        putSavedBoolean(PREF_ENABLE_RECORDS, value);
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
        if (context == null) {
            return;
        }

        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
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

    static public byte[] drawableToByteArray(Drawable drawable) {
        if (drawable == null) return null;

        // Convert Drawable to Bitmap
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        // Convert Bitmap to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    static public Bitmap byteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    static public SearchEngine getSelectedSearchEngine(List<SearchEngine> engines) {
        String selectedLabel = getSavedSearchEngineLabel();
        for (int i = 0; i < engines.size(); i++) {
            if (engines.get(i).label.equals(selectedLabel)) {
                return engines.get(i);
            }
        }

        return engines.get(0);
    }

    static public List<String> getRecordList(int size) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(getSavedString(PREF_RECORD_ENTRY + i, ""));
        }

        return data;
    }

    static public void saveRecordList(List<String> data) {
        SharedPreferences.Editor editSession = sharedPref.edit();

        int i=0;
        for (String text : data) {
            editSession.putString(PREF_RECORD_ENTRY + i, text);
            i++;
        }

        editSession.putInt(PREF_RECORD_LIST_SIZE, i);

        editSession.apply();
    }

    static public int getSelectedSearchEngineIndex(List<SearchEngine> engines) {
        SearchEngine selected = getSelectedSearchEngine(engines);
        return engines.indexOf(selected);
    }

    static public List<SearchEngine> getOnlyShownSearchEngines(List<SearchEngine> engines) {
        List<SearchEngine> filteredList = new ArrayList<>();

        for (SearchEngine item: engines) {
            if (!item.hidden) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    public static void viewExpand(final View v, @Nullable Runnable onEndListener) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        // it also makes animation more smooth
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }


        };

        if (onEndListener != null) {
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    onEndListener.run();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }

        // 1dp/ms
        a.setDuration(200);
        v.startAnimation(a);
    }

    public static void viewCollapse(final View v, Runnable runOnEnd) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(200);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (runOnEnd != null) {
                    runOnEnd.run();
                }
            }
        });
        v.startAnimation(a);
    }
}
