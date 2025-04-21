package de.tobiasbielefeld.searchbar.helper;

import static de.tobiasbielefeld.searchbar.SharedData.PREF_CUSTOM_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.PREF_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedSearchEngineKey;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedString;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedSearchEngineKey;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedString;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.SearchEngineItem;

public class SearchEngines {

    public static final int NUM_CUSTOM_SEARCH_ENGINES = 3;
    public static final String CUSTOM_SEARCH_KEY_PREFIX = "search_custom_";

    private static SearchEngines searchEngines;
    private final List<SearchEngineItem> items;

    public static SearchEngines get(Resources res) {
        if (searchEngines == null) {
            searchEngines = new SearchEngines(res);
        }

        return searchEngines;
    }

    private SearchEngines(Resources res) {
        items = Arrays.asList(
            new SearchEngineItem(res.getString(R.string.settings_custom), getSavedString(PREF_CUSTOM_SEARCH_URL, ""), CUSTOM_SEARCH_KEY_PREFIX + 0, R.drawable.icon_counter_one, R.drawable.icon_counter_one_search_bar),
            new SearchEngineItem(res.getString(R.string.settings_custom), getSavedString(PREF_CUSTOM_SEARCH_URL + "1", ""), CUSTOM_SEARCH_KEY_PREFIX + 1, R.drawable.icon_counter_two, R.drawable.icon_counter_two_search_bar),
            new SearchEngineItem(res.getString(R.string.settings_custom), getSavedString(PREF_CUSTOM_SEARCH_URL + "2", ""), CUSTOM_SEARCH_KEY_PREFIX + 2, R.drawable.icon_counter_three, R.drawable.icon_counter_three_search_bar),
            new SearchEngineItem("Bing", res.getString(R.string.url_bing), "search_bing", R.drawable.favicon_bing),
            new SearchEngineItem("DuckDuckGo", res.getString(R.string.url_duckduckgo), "search_duckduckgo", R.drawable.favicon_duckduckgo),
            new SearchEngineItem("DuckDuckGo Lite", res.getString(R.string.url_duckduckgo_lite), "search_duckduckgo_lite", R.drawable.favicon_duckduckgo_lite),
            new SearchEngineItem("Ecosia", res.getString(R.string.url_ecosia), "search_ecosia", R.drawable.favicon_ecosia),
            new SearchEngineItem("Google", res.getString(R.string.url_google), "search_google", R.drawable.favicon_google),
            new SearchEngineItem("Metager", res.getString(R.string.url_metager), "search_metager", R.drawable.favicon_metager),
            new SearchEngineItem("metasearX", res.getString(R.string.url_searx), "search_metasearx", R.drawable.favicon_metasearchx),
            new SearchEngineItem("Startpage", res.getString(R.string.url_startpage), "search_startpage", R.drawable.favicon_startpage),
            new SearchEngineItem("Yahoo", res.getString(R.string.url_yahoo), "search_yahoo", R.drawable.favicon_yahoo),
            new SearchEngineItem("Yandex", res.getString(R.string.url_yandex), "search_yandex", R.drawable.favicon_yandex)
        );
    }

    public void updateCustomSearchUris() {
        for (int i = 0; i < NUM_CUSTOM_SEARCH_ENGINES; i++) {
            items.get(i).updateUri(getSavedString(i > 0 ? PREF_CUSTOM_SEARCH_URL + i : PREF_CUSTOM_SEARCH_URL, ""));
        }
    }

    public List<SearchEngineItem> items() {
        return items;
    }

    public int selectedIndex() {
        // fallback for older way to save selected search engine
        int oldIndex = getOldIndex();
        if (oldIndex != -1) {
            return oldIndex;
        }

        String selectedKey = getSavedSearchEngineKey();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).key().equals(selectedKey)) {
                return i;
            }
        }

        return NUM_CUSTOM_SEARCH_ENGINES; // first non-custom search engine is the default
    }

    public SearchEngineItem selectedItem() {
        int selectedIndex = selectedIndex();
        return items.get(selectedIndex);
    }

    public int getOldIndex() {
        String oldSelected = getSavedString(PREF_SEARCH_URL, "");

        if (oldSelected.isEmpty()) {
            return -1; // no old version is saved
        }

        for (SearchEngineItem item: items) {
            if (item.uri().equals(oldSelected)) {
                putSavedSearchEngineKey(item.key()); // save in new version
                putSavedString(PREF_SEARCH_URL, ""); // overwrite old version
                return items.indexOf(item);
            }
        }

        return -1;
    }
}
