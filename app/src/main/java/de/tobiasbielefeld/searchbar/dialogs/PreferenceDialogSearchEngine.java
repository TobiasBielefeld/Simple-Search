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

package de.tobiasbielefeld.searchbar.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import de.tobiasbielefeld.searchbar.R;
import static de.tobiasbielefeld.searchbar.SharedData.*;

/**
 * Acts like a normal list preference, but when pressing the "custom" entry, a new dialog shows
 * to enter a custom search engine. In that case, the entered url will be shown as the summary.
 */

public class PreferenceDialogSearchEngine extends ListPreference{

    private EditText customEditText;

    private int clickedDialogEntryIndex = -1;

    public PreferenceDialogSearchEngine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);

        updateSummary();

        return view;
    }

    /**
     * If a custom search engine was selected, show its url, else show the title of the engine
     */
    private void updateSummary(){
        int d = getContext().getResources().getInteger(R.integer.default_search_engine_v2);
        String selectedValue = getPersistedString(d + "");
        int index = findIndexOfValue(selectedValue);

        if (index == 0){    //custom search engine
            String summary = getSavedString(PREF_SEARCH_URL,DEFAULT_SEARCH_URL);
            summary = summary.replace("%","%%");    //used to escape the %-character in the summary
            setSummary(summary);
        } else {            //a search engine from the list
            String[] searchUris = getContext().getResources().getStringArray(R.array.search_engine_uris);
            putSavedString(PREF_SEARCH_URL,searchUris[index]);
            setSummary(getEntries()[index]);
        }
    }

    /**
     * THe user can enter a custom search engine url here
     */
    private void showCustomSearchEngineDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_search_engine_dialog, null);

        customEditText = (EditText) v.findViewById(R.id.custom_search_url);
        customEditText.setText(getSavedString(PREF_SEARCH_URL,DEFAULT_SEARCH_URL));

        builder.setView(v);
        builder.setTitle(R.string.dialog_custom_search_engine_title)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        putSavedString(PREF_SEARCH_URL,customEditText.getText().toString());
                        clickedDialogEntryIndex = 0;
                        getDialog().dismiss();
                    }})
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancel
                    }});

        builder.show();
    }


    /*
     * Change what happens when pressing an entry. If "Custom" was clicked, show its dialog,
     * else act like a normal list dialog
     */
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        clickedDialogEntryIndex = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(getEntries(), clickedDialogEntryIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        if(which ==0){
                            showCustomSearchEngineDialog();
                        }
                        else{
                            clickedDialogEntryIndex = which;
                            dialog.dismiss();
                        }
                    }
                });

        builder.setPositiveButton(null, null);
    }

    /*
     * Saves the selected value when closing the dialog. If none was selected, the
     * clickedDialogEntryIndex is still -1, so nothing happens
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {

        if (clickedDialogEntryIndex >= 0) {
            String value = getEntryValues()[clickedDialogEntryIndex].toString();

            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }
}
