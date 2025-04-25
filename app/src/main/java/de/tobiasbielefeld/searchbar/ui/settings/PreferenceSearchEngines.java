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

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import java.util.List;

import de.tobiasbielefeld.searchbar.classes.SearchEngineItem;
import de.tobiasbielefeld.searchbar.helper.SearchEngines;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;


public class PreferenceSearchEngines extends CustomListPreference {

    private final SearchEngines searchEngines;

    public PreferenceSearchEngines(@NonNull Context context) {
        super(context);
        searchEngines = SearchEngines.get(context.getResources());
    }

    public PreferenceSearchEngines(Context context, AttributeSet attrs) {
        super(context, attrs);
        searchEngines = SearchEngines.get(context.getResources());
    }

    public PreferenceSearchEngines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        searchEngines = SearchEngines.get(context.getResources());
    }

    @Override
    protected int getCurrentValue() {
        return searchEngines.selectedIndex();
    }

    @Override
    public int getTitleArrayId() {
        return -1; // unused, we overwrite getTitles() instead
    }

    @Override
    public String[] getTitles() {
        List<SearchEngineItem> items = searchEngines.items();
        String[] titles = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            titles[i] = items.get(i).label();
        }

        return titles;
    }

    @Override
    public void updateSummary(boolean positiveResult) {
        searchEngines.updateCustomSearchUris();
        SearchEngineItem item = searchEngines.selectedItem();
        setSummary(item.isCustomEngine() ? item.uri() : item.label());
    }
}
