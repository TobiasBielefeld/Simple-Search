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

package de.tobiasbielefeld.searchbar.helper;

import android.content.res.Resources;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Collections;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.SharedData;
import de.tobiasbielefeld.searchbar.dialogs.DialogDeleteAll;
import de.tobiasbielefeld.searchbar.dialogs.DialogDeleteEntry;
import de.tobiasbielefeld.searchbar.ui.MainActivity;

import static de.tobiasbielefeld.searchbar.SharedData.*;

/**
 * Manages the Records of the search queries.
 */

public class Records implements View.OnClickListener, View.OnLongClickListener{

    private final MainActivity main;
    private final LinearLayout container;
    private final LinkedList<String> recordList;
    private final int MAX_NUMBER_OF_RECORDS;

    public Records(MainActivity mainActivity, LinearLayout linearLayout){
        container = linearLayout;
        main = mainActivity;
        Resources res = main.getResources();
        MAX_NUMBER_OF_RECORDS = res.getInteger(R.integer.max_number_records);
        recordList = new LinkedList<>();
    }

    /**
     * Load the linearLayouts of the records in an array list, hides everything first, then
     * loads the data from the sharedPref and applies them to the layout entries.
     */
    public void load() {

        recordList.clear();
        container.removeAllViews();

        if (recordsDisabled())
            return;

        int recordListLength = getSavedInt(PREF_RECORD_LIST_SIZE, 0);

        // populate linearLayouts with layouts from XML resource
        for (int i = 0; i < recordListLength; i++) {
            LinearLayout layout= (LinearLayout) LayoutInflater.from(main).inflate(R.layout.record_list_entry, null);
            TextView textView = (TextView) layout.getChildAt(0);

            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            layout.getChildAt(1).setOnClickListener(this);

            String text = getSavedString(PREF_RECORD_ENTRY + i, "");

            // Crosslink elements
            recordList.add(text);
            ((TextView) layout.getChildAt(0)).setText(text);

            container.addView(layout);
        }
    }

    /**
     * Adds a new query to the list and deletes the oldest entry if the maximum number was reached
     *
     * @param newString The new query to save
     */
    public void add(String newString) {

        if (recordsDisabled())
            return;

        // Prevent redundant entries from filling history
        recordList.removeAll(Collections.singleton(newString));

        recordList.addFirst(newString);

        while (recordList.size() > MAX_NUMBER_OF_RECORDS) {
            recordList.removeLast();
        }

        save();
    }

    /**
     * Saves the record list to the sharedPref
     */
    private void save() {
        // Start edit session for preferences to prevent inconsistencies in stored data.
        SharedPreferences.Editor editSession = SharedData.sharedPref.edit();
        
        int i=0;
        for (String text : recordList) {
            editSession.putString(PREF_RECORD_ENTRY + i, text);
            i++;
        }

        editSession.putInt(PREF_RECORD_LIST_SIZE, i);
        
        editSession.apply();
    }

    /**
     * Deletes the given record from the list, and applies it by simply saving and loading after that
     *
     * @param text The record to delete
     */
    public void delete(String text){
        recordList.removeAll(Collections.singleton(text));
        save();
        load();
    }

    /**
     * Deltes the entire record list and applies the changes
     */
    public void deleteAll(){
        recordList.clear();
        save();
        load();
    }

    private void showDeleteEntryDialog(String text) {
        DialogDeleteEntry dialog = new DialogDeleteEntry();
        dialog.setText(text);

        dialog.show(main.getSupportFragmentManager(), "dialog");
    }

    public void showDeleteAllDialog() {
        DialogDeleteAll dialog = new DialogDeleteAll();
        dialog.show(main.getSupportFragmentManager(), "dialog2");
    }

    private boolean recordsDisabled(){
        return !getSavedBoolean(main.getString(R.string.pref_key_enable_records), true);
    }

    /*
     * Do different stuff depending on the clicked view
     */
    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
           onRecordTextClick(v);
        } else if (v instanceof ImageView){
            onRecordButtonClick(v);
        }
    }

    /*
     *  shows the delete entry dialog when long clicking an entry
     */
    @Override
    public boolean onLongClick(View v) {
        String text = ((TextView) v).getText().toString();
        showDeleteEntryDialog(text);

        return true;
    }

    /**
     * Sets the record of the clicked view to the search bar, and starts the search with it
     *
     * @param v The clicked view
     */
    private void onRecordTextClick(View v){
        String text = ((TextView) v).getText().toString();

        main.setSearchText(text);
        main.startSearch();
    }

    /**
     * Appends the record of the clicked view to the search bar and refocus the bar
     *
     * @param v The clicked view
     */
    private void onRecordButtonClick(View v){
        LinearLayout parent = (LinearLayout) v.getParent();
        String text = ((TextView) parent.getChildAt(0)).getText().toString();
        main.appendSearchText(text);
        main.focusSearchBar();
    }
}
