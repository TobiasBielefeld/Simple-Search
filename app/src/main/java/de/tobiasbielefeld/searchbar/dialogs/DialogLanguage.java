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
