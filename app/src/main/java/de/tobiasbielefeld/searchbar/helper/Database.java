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

package de.tobiasbielefeld.searchbar.helper;

import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;


import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.models.SearchEngine;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "simple_searchbar_database";
    public static final int DATABASE_VERSION = 6;

    private final SQLiteDatabase database;
    private final Context context;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SearchEngine.createTable(db, context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SearchEngine.onUpgrade(db, oldVersion, newVersion, context);
    }

    public void withTransaction(final View postableView, final Context context, final Runnable task) {
        database.beginTransaction();

        try {
            task.run();
            database.setTransactionSuccessful();
        } catch (SQLiteFullException e) {
            postableView.post(() -> showToast(context.getString(R.string.db_save_no_disk_space_available), context));
        } finally {
            database.endTransaction();
        }
    }

    public List<SearchEngine> getSearchEngines() {
        return SearchEngine.getEntries(database);
    }

    public void putSearchEngine(SearchEngine entry) {
        SearchEngine.putEntry(database, entry);
    }

    public void deleteSearchEngine(SearchEngine entry) {
        SearchEngine.deleteEntry(database, entry);
    }
}
