

package de.tobiasbielefeld.searchbar.ui.settings;

import static de.tobiasbielefeld.searchbar.SharedData.DEFAULT_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.PREF_CUSTOM_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.PREF_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedString;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;


public class PreferenceSearchEngines extends CustomListPreference {

    Context context;

    public PreferenceSearchEngines(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public PreferenceSearchEngines(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PreferenceSearchEngines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected int getCurrentValue() {
        int d = getContext().getResources().getInteger(R.integer.default_search_engine_v2);
        return Integer.parseInt(getPersistedString(d + ""));
    }

    @Override
    public int getTitleArrayId() {
        return R.array.pref_search_engine_titles;
    }

    @Override
    public void updateSummary(boolean positiveResult) {
        String[] titles = context.getResources().getStringArray(getTitleArrayId());
        int selectedValue = getCurrentValue();

        if (selectedValue < 3) {    //custom search engine
            String prefKey = selectedValue > 0 ? PREF_CUSTOM_SEARCH_URL + selectedValue : PREF_CUSTOM_SEARCH_URL;
            String savedSearchUrl = getSavedString(PREF_SEARCH_URL,DEFAULT_SEARCH_URL);
            String summary = getSavedString(prefKey, savedSearchUrl);
            setSummary(summary);
        } else {            // a search engine from the list
            setSummary(titles[selectedValue]);
        }
    }
}
