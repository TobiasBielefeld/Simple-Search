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

import static de.tobiasbielefeld.searchbar.SharedData.database;
import static de.tobiasbielefeld.searchbar.SharedData.getSelectedSearchEngineIndex;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedSearchEngineLabel;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.tobiasbielefeld.searchbar.models.SearchEngine;

public class DialogSelectedSearchEngine extends CustomPreferenceListFragmentCompat {

    private List<SearchEngine> searchEngines;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        searchEngines = database.getSearchEngines();
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    protected void selectionChanged() {
        putSavedSearchEngineLabel(searchEngines.get(selectedIndex).label);
        dismiss();
    }

    @Override
    protected int getSelectedIndex() {
        return getSelectedSearchEngineIndex(searchEngines);
    }

    @Override
    public String[] getTitles() {
        String[] titles = new String[searchEngines.size()];

        for (int i = 0; i < searchEngines.size(); i++) {
            titles[i] = searchEngines.get(i).label;
        }

        return titles;
    }

}
