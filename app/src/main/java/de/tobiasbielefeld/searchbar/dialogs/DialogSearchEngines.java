/*
 * Copyright (C) 2024  Tobias Bielefeld
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

import static de.tobiasbielefeld.searchbar.SharedData.PREF_CUSTOM_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedString;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedSearchEngineKey;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedString;
import static de.tobiasbielefeld.searchbar.helper.SearchEngines.CUSTOM_SEARCH_KEY_PREFIX;
import static de.tobiasbielefeld.searchbar.helper.SearchEngines.NUM_CUSTOM_SEARCH_ENGINES;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.helper.SearchEngines;

public class DialogSearchEngines extends CustomPreferenceListFragmentCompat {

    private EditText customEditText;

    @Override
    protected void selectionChanged() {
        if (selectedIndex < NUM_CUSTOM_SEARCH_ENGINES) {
            showCustomSearchEngineDialog(selectedIndex);
        } else {
            putSavedSearchEngineKey(SearchEngines.get(getResources()).items().get(selectedIndex).key());
            dismiss();
        }
    }

    @Override
    protected int getSelectedIndex() {
        return SearchEngines.get(getResources()).selectedIndex();
    }

    private void showCustomSearchEngineDialog(final int which){
        final String prefKey = which > 0 ? PREF_CUSTOM_SEARCH_URL + which : PREF_CUSTOM_SEARCH_URL;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_search_engine_dialog, null);

        customEditText = v.findViewById(R.id.custom_search_url);
        customEditText.setText(getSavedString(prefKey, ""));

        builder.setView(v);
        builder.setTitle(R.string.dialog_custom_search_engine_title)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    String text = customEditText.getText().toString();
                    putSavedSearchEngineKey(CUSTOM_SEARCH_KEY_PREFIX + which);
                    putSavedString(prefKey, text);
                    selectedIndex = which;
                    dialog.dismiss();
                    dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //cancel
                });

        builder.show();
    }
}
