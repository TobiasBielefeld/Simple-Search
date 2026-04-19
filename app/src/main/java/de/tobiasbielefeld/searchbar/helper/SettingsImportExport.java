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

package de.tobiasbielefeld.searchbar.helper;

import static de.tobiasbielefeld.searchbar.SharedData.*;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.tobiasbielefeld.searchbar.models.export.AppSettingsExport;
import de.tobiasbielefeld.searchbar.models.export.AppSettingsExportV2;
import de.tobiasbielefeld.searchbar.models.export.SearchEngineExport;

public class SettingsImportExport {
    public static AppSettingsExportV2 load() {
        AppSettingsExportV2 model = new AppSettingsExportV2();
        model.searchEngines = database.getSearchEnginesForExport();
        model.searchEngineLabel = getSavedSearchEngineLabel();
        model.theme = getSavedTheme();
        model.orientation = getSavedOrientation();
        model.toolbarColor = getSavedToolbarColor();
        model.closeAfterSearch = getSavedCloseAfterSearch();
        model.edgeToEdgeDisplayMode = getSavedEdgeToEdgeDisplayMode();
        model.keepSelectedSearchEngine = getSavedKeepSelectedSearchEngine();
        model.startOnQuickSearchSelect = getSavedStartOnQuickSearchSelect();
        model.enableRecords = getSavedEnableRecords();
        model.statusBar = getSavedStatusBar();
        model.language = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        model.recordList = getRecordList(getSavedRecordListSize());
        model.searchbarPosition = getSavedSearchbarPosition();

        return model;
    }

    public static void apply(AppSettingsExport model) {
        for (SearchEngineExport engine: model.searchEngines) {
            database.putSearchEngine(engine);
        }

        putSavedSearchEngineLabel(model.searchEngineLabel);
        putSavedTheme(model.theme);
        putSavedOrientation(model.orientation);
        putSavedToolbarColor(model.toolbarColor);
        putSavedCloseAfterSearch(model.closeAfterSearch);
        putSavedEdgeToEdgeDisplayMode(model.edgeToEdgeDisplayMode);
        putSavedKeepSelectedSearchEngine(model.keepSelectedSearchEngine);
        putSavedStartOnQuickSearchSelect(model.startOnQuickSearchSelect);
        putSavedEnableRecords(model.enableRecords);
        putSavedStatusBar(model.statusBar);
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(model.language));
        saveRecordList(model.recordList);
    }

    public static void applyV2(AppSettingsExportV2 model) {
        apply(model);
        putSavedSearchbarPosition(model.searchbarPosition);
    }

    public static String toJson() {
        return new Gson().toJson(load());
    }

    public static void fromJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        int version = root.get("version").getAsInt();

        switch (version) {
            case 1:
                apply(new Gson().fromJson(root, AppSettingsExport.class));
                break;
            case 2:
                applyV2(new Gson().fromJson(root, AppSettingsExportV2.class));
                break;
        }
    }
}
