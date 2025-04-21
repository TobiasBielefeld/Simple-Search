/*
 * Copyright (C) 2025  Tobias Bielefeld
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