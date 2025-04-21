

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
