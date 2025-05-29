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

import static de.tobiasbielefeld.searchbar.SharedData.showOrHideStatusBar;
import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.helper.SettingsImportExport;
import de.tobiasbielefeld.searchbar.manageSearchEngines.DialogManageSearchEngines;
import de.tobiasbielefeld.searchbar.dialogs.DialogShowSearchEngines;
import de.tobiasbielefeld.searchbar.dialogs.DialogLanguage;
import de.tobiasbielefeld.searchbar.dialogs.DialogSelectedSearchEngine;
import de.tobiasbielefeld.searchbar.dialogs.DialogToolbarColor;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomDialogPreference;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomPreferenceFragmentCompat;

public class FragmentSettings extends CustomPreferenceFragmentCompat {

    private ActivityResultLauncher<Intent> exportLauncher, importLauncher;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        setPreferencesFromResource(R.xml.pref_settings, rootKey);

        ListPreference theme = findPreference(getString(R.string.pref_key_theme));
        ListPreference screenOrientation = findPreference(getString(R.string.pref_key_orientation));
        PreferenceLanguage language = findPreference(getString(R.string.pref_key_language));
        PreferenceSelectedSearchEngine searchEngines = findPreference(getString(R.string.pref_key_search_engine));
        Preference fullscreen = findPreference(getString(R.string.pref_key_hide_status_bar));
        CustomDialogPreference toolbarColor = findPreference(getString(R.string.pref_key_toolbar_color));
        Preference useEdgeToEdgeDisplayMode = findPreference(getString(R.string.pref_key_edge_to_edge_display_mode));
        CustomDialogPreference showSearchEngines = findPreference(getString(R.string.pref_key_show_search_engines));
        CustomDialogPreference manageCustomSearchEngines = findPreference(getString(R.string.pref_key_manage_search_engines));

        Preference exportSettings = findPreference(getString(R.string.pref_key_export_settings));
        Preference importSettings = findPreference(getString(R.string.pref_key_import_settings));

        bindDialog(searchEngines, DialogSelectedSearchEngine.class);
        bindDialog(language, DialogLanguage.class);
        bindDialog(toolbarColor, DialogToolbarColor.class);
        bindDialog(showSearchEngines, DialogShowSearchEngines.class);
        bindDialog(manageCustomSearchEngines, DialogManageSearchEngines.class);

        assert screenOrientation != null;
        screenOrientation.setOnPreferenceChangeListener((Preference pref, Object newValue) -> {
            if (getActivity() instanceof CustomAppCompatActivity activity) {
                int newVal = Integer.parseInt((String) newValue);
                activity.setOrientation(newVal);
            }

            return true;
        });

        assert theme != null;
        theme.setOnPreferenceChangeListener((Preference pref, Object newValue) -> {
            requireActivity().recreate();
            return true;
        });

        assert fullscreen != null;
        fullscreen.setOnPreferenceChangeListener((Preference pref, Object newValue) -> {
            showOrHideStatusBar(requireActivity().getWindow(), (boolean) newValue);
            return true;
        });

        assert useEdgeToEdgeDisplayMode != null;
        useEdgeToEdgeDisplayMode.setOnPreferenceChangeListener((Preference pref, Object newValue) -> {
            Intent resultIntent = ((Settings) requireActivity()).resultIntent;
            resultIntent.putExtra("RELOAD_EDGE_TO_EDGE", true);
            return true;
        });

        assert exportSettings != null;
        exportSettings.setOnPreferenceClickListener((Preference pref) -> {
            startExport();
            return true;
        });

        assert importSettings != null;
        importSettings.setOnPreferenceClickListener((Preference pref) -> {
            startImport();
            return true;
        });

        exportLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    exportJsonToUri(uri);
                }
            }
        });

        importLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    importJsonFromUri(uri);
                }
            }
        });
    }


    private void startExport() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "simple_search_export.json");

        Context context = requireContext();
        showToast(context.getString(R.string.settings_export_message_hint), context);
        exportLauncher.launch(intent);
    }

    public void startImport() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");

        Context context = requireContext();
        showToast(context.getString(R.string.settings_import_message_hint), context);
        importLauncher.launch(intent);
    }

    private void exportJsonToUri(Uri uri) {
        Context context = requireContext();

        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
            outputStream.write(SettingsImportExport.toJson().getBytes(StandardCharsets.UTF_8));
            showToast(context.getString(R.string.settings_export_message_success), context);
        } catch (IOException e) {
            showToast(context.getString(R.string.settings_export_message_error), context);
        }
    }

    private void importJsonFromUri(Uri uri) {
        Context context = requireContext();

        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String jsonContent = builder.toString();
            SettingsImportExport.fromJson(jsonContent);

            showToast(context.getString(R.string.settings_import_message_success), context);
        } catch (Exception e) {
            showToast(context.getString(R.string.settings_import_message_error), context);
        }
    }
}

