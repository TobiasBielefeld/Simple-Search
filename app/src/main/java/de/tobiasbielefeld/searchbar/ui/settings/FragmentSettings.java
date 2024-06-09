package de.tobiasbielefeld.searchbar.ui.settings;

import static de.tobiasbielefeld.searchbar.SharedData.showOrHideStatusBar;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.dialogs.DialogLanguage;
import de.tobiasbielefeld.searchbar.dialogs.DialogSearchEngines;
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

        bindDialog(searchEngines, DialogSearchEngines.class);
        bindDialog(language, DialogLanguage.class);

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
            if (getActivity() != null) {
                getActivity().recreate();
            }

            return true;
        });

        assert fullscreen != null;
        fullscreen.setOnPreferenceChangeListener((Preference pref, Object newValue) -> {
            if (getActivity() != null) {
                showOrHideStatusBar(requireActivity().getWindow(), (boolean) newValue);
            }

            return true;
        });
    }

}

