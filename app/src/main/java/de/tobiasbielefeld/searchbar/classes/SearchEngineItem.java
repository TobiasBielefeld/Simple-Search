package de.tobiasbielefeld.searchbar.classes;

import static de.tobiasbielefeld.searchbar.helper.SearchEngines.CUSTOM_SEARCH_KEY_PREFIX;

public class SearchEngineItem {
    private final String label;
    private final String key;
    private final int iconRes;
    private final int searchbarIconRes;
    private String uri;

    public SearchEngineItem(String label, String uri, String key, int iconRes, int searchbarIconRes) {
        this.label = label;
        this.uri = uri;
        this.key = key;
        this.iconRes = iconRes;
        this.searchbarIconRes = searchbarIconRes;
    }

    public SearchEngineItem(String label, String uri, String key, int iconRes) {
        this.label = label;
        this.uri = uri;
        this.key = key;
        this.iconRes = iconRes;
        this.searchbarIconRes = -1;
    }

    public boolean isCustomEngine() {
        return key.startsWith(CUSTOM_SEARCH_KEY_PREFIX);
    }

    public boolean isUriNotValid() {
        return !uri.contains("%s");
    }

    public void updateUri(String uri) {
        this.uri = uri;
    }

    public String label() {
        return label;
    }

    public String uri() {
        return uri;
    }

    public String key() {
        return key;
    }

    public int iconRes() {
        return iconRes;
    }

    public int searchbarIconRes() {
        return searchbarIconRes;
    }
}