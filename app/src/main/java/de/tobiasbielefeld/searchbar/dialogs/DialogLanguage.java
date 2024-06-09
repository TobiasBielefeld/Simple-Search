package de.tobiasbielefeld.searchbar.dialogs;

import static de.tobiasbielefeld.searchbar.SharedData.findBestLocaleMatchIndex;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import de.tobiasbielefeld.searchbar.R;

public class DialogLanguage extends CustomPreferenceListFragmentCompat {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void selectionChanged() {
        String[] availableLanguages = getResources().getStringArray(R.array.pref_language_values);
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(availableLanguages[selectedIndex]));
        dismiss();
    }

    @Override
    protected int getSelectedIndex() {
        String[] availableLanguages = getResources().getStringArray(R.array.pref_language_values);
        String systemLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        return findBestLocaleMatchIndex(availableLanguages, systemLanguage);
    }
}
