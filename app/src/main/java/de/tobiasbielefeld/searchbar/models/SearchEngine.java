/*
 * Copyright (C) 2025  Tobias Bielefeld
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

package de.tobiasbielefeld.searchbar.models;

import static de.tobiasbielefeld.searchbar.SharedData.drawableToByteArray;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedBoolean;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedString;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedSearchEngineLabel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;

public class SearchEngine {
    static public final String TABLE_SEARCH_ENGINE = "search_engine";
    static public final String COLUMN_LABEL = "label";
    static public final String COLUMN_URI = "uri";
    static public final String COLUMN_HIDDEN = "hidden";
    static public final String COLUMN_ICON = "icon";
    public String label, uri;
    public byte[] icon;
    public boolean hidden;

    public SearchEngine(Cursor cursor) {
        this.label = cursor.getString(0);
        this.uri = cursor.getString(1);
        this.hidden = cursor.getInt(2) == 1;
        this.icon = cursor.getBlob(3);
    }

    public SearchEngine(String label, String uri, boolean hidden, byte[] icon) {
        this.label = label;
        this.uri = uri;
        this.hidden = hidden;
        this.icon = icon;
    }


    public static void createTable(SQLiteDatabase db, Context ctx) {
        db.execSQL(String.format("CREATE TABLE %s (%s TEXT, %s TEXT, %s INTEGER, %s BLOB, PRIMARY KEY (%s))", TABLE_SEARCH_ENGINE, COLUMN_LABEL, COLUMN_URI, COLUMN_HIDDEN, COLUMN_ICON, COLUMN_LABEL));

        importLegacyEntry(db, ctx, "search_bing", "Bing", "https://www.bing.com/search?q=%s", "search_bing_shown", R.drawable.favicon_bing);
        importLegacyEntry(db, ctx, "search_duckduckgo", "DuckDuckGo", "https://duckduckgo.com/?q=%s", "search_duckduckgo_shown", R.drawable.favicon_duckduckgo);
        importLegacyEntry(db, ctx, "search_duckduckgo_lite", "DuckDuckGo Lite", "https://duckduckgo.com/lite?q=%s", "search_duckduckgo_lite_shown", R.drawable.favicon_duckduckgo_lite);
        importLegacyEntry(db, ctx, "search_ecosia", "Ecosia", "https://www.ecosia.org/search?q=%s", "search_ecosia_shown", R.drawable.favicon_ecosia);
        importLegacyEntry(db, ctx, "search_google", "Google", "https://www.google.com/search?q=%s", "search_google_shown", R.drawable.favicon_google);
        importLegacyEntry(db, ctx, "search_metager", "Metager", "https://metager.de/meta/meta.ger3?eingabe=%s", "search_metager_shown", R.drawable.favicon_metager);
        importLegacyEntry(db, ctx, "search_metasearx", "metasearX", "https://metasearx.com/?q=%s", "search_metasearx_shown", R.drawable.favicon_metasearchx);
        importLegacyEntry(db, ctx, "search_qwant", "Qwant", "https://www.qwant.com/?q=%s", "search_qwant_shown", R.drawable.favicon_qwant);
        importLegacyEntry(db, ctx, "search_startpage", "Startpage", "https://www.startpage.com/do/search?query=%s", "search_startpage_shown", R.drawable.favicon_startpage);
        importLegacyEntry(db, ctx, "search_yahoo", "Yahoo", "https://search.yahoo.com/search?p=%s", "search_yahoo_shown", R.drawable.favicon_yahoo);
        importLegacyEntry(db, ctx, "search_yandex", "Yandex", "https://www.yandex.com/search/?text=%s", "search_yandex_shown", R.drawable.favicon_yandex);

        importLegacyCustomEntry(db, ctx,
                "pref_key_custom_search_url",
                "search_custom_0",
                ctx.getResources().getString(R.string.settings_custom) + " 1",
                "search_custom_0_shown",
                R.drawable.icon_counter_one_search_bar
        );

        importLegacyCustomEntry(db, ctx,
                "pref_key_custom_search_url1",
                "search_custom_1",
                ctx.getResources().getString(R.string.settings_custom) + " 2",
                "search_custom_1_shown",
                R.drawable.icon_counter_two_search_bar
        );

        importLegacyCustomEntry(db, ctx,
                "pref_key_custom_search_url2",
                "search_custom_2",
                ctx.getResources().getString(R.string.settings_custom) + " 3",
                "search_custom_2_shown",
                R.drawable.icon_counter_three_search_bar
        );
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion, Context context) {
        // no upgrades yet
    }

    public static List<SearchEngine> getEntries(SQLiteDatabase db) {
        List<SearchEngine> entries = new ArrayList<>();

        try (Cursor cursor = db.query(TABLE_SEARCH_ENGINE, null, null, null, null, null, COLUMN_LABEL + " COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) {
                entries.add(new SearchEngine(cursor));
            }
        }

        return entries;
    }

    public static void putEntry(SQLiteDatabase db, SearchEngine entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, entry.label);
        values.put(COLUMN_URI, entry.uri);
        values.put(COLUMN_HIDDEN, entry.hidden ? 1 : 0);
        values.put(COLUMN_ICON, entry.icon);

        db.insertWithOnConflict(TABLE_SEARCH_ENGINE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void deleteEntry(SQLiteDatabase db, SearchEngine entry) {
        String selection = String.format("%s = ?", COLUMN_LABEL);
        db.delete(TABLE_SEARCH_ENGINE, selection, new String[]{entry.label});
    }

    private static void importLegacyEntry(SQLiteDatabase db, Context ctx, String oldSearchKey, String newLabel, String uri, String shownKey, int resId) {
        if (getSavedString("search_key", "").equals(oldSearchKey) || getSavedString("search_url", "").equals(uri)) {
            putSavedSearchEngineLabel(newLabel);
        }

        putEntry(db, new SearchEngine(newLabel, uri, !getSavedBoolean(shownKey, true), drawableToByteArray(AppCompatResources.getDrawable(ctx, resId))));
    }

    private static void importLegacyCustomEntry(SQLiteDatabase db, Context ctx, String oldSearchUrl, String oldSearchKey, String label, String shownKey, int resId) {
        String savedCustomSearchUrl = getSavedString(oldSearchKey, getSavedString(oldSearchUrl, ""));

        if (!savedCustomSearchUrl.isEmpty()) {
            if (getSavedString("search_key", "").equals(oldSearchKey)) {
                putSavedSearchEngineLabel(label);
            }

            putEntry(db, new SearchEngine(label, savedCustomSearchUrl, !getSavedBoolean(shownKey, true), drawableToByteArray(AppCompatResources.getDrawable(ctx, resId))));
        }
    }
}
