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

package de.tobiasbielefeld.searchbar.ui.settings;

import static de.tobiasbielefeld.searchbar.SharedData.database;
import static de.tobiasbielefeld.searchbar.SharedData.getSelectedSearchEngine;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import de.tobiasbielefeld.searchbar.models.SearchEngine;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;

public class PreferenceSelectedSearchEngine extends CustomListPreference {

    public PreferenceSelectedSearchEngine(@NonNull Context context) {
        super(context);
    }

    public PreferenceSelectedSearchEngine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceSelectedSearchEngine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getCurrentValue() {
        return -1; // unused
    }

    @Override
    public int getTitleArrayId() {
        return -1; // unused
    }

    @Override
    public void updateSummary(boolean positiveResult) {
        SearchEngine selected = getSelectedSearchEngine(database.getSearchEngines());
        setSummary(selected.label);
    }
}
