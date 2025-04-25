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

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.dialogs.DialogShowSearchEngines;
import de.tobiasbielefeld.searchbar.dialogs.DialogLanguage;
import de.tobiasbielefeld.searchbar.dialogs.DialogSearchEngines;
import de.tobiasbielefeld.searchbar.dialogs.DialogToolbarColor;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomDialogPreference;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomPreferenceFragmentCompat;

public class FragmentSettings extends CustomPreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        setPreferencesFromResource(R.xml.pref_settings, rootKey);

        ListPreference theme = findPreference(getString(R.string.pref_key_theme));
        ListPreference screenOrientation = findPreference(getString(R.string.pref_key_orientation));
        PreferenceLanguage language = findPreference(getString(R.string.pref_key_language));
        PreferenceSearchEngines searchEngines = findPreference(getString(R.string.pref_key_search_engine));
        Preference fullscreen = findPreference(getString(R.string.pref_key_hide_status_bar));
        CustomDialogPreference toolbarColor = findPreference(getString(R.string.pref_key_toolbar_color));
        Preference useEdgeToEdgeDisplayMode = findPreference(getString(R.string.pref_key_edge_to_edge_display_mode));
        CustomDialogPreference showSearchEngines = findPreference(getString(R.string.pref_key_show_search_engines));

        bindDialog(searchEngines, DialogSearchEngines.class);
        bindDialog(language, DialogLanguage.class);
        bindDialog(toolbarColor, DialogToolbarColor.class);
        bindDialog(showSearchEngines, DialogShowSearchEngines.class);

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
    }

}

