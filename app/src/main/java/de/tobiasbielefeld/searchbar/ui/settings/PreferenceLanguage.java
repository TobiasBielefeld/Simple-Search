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

import static de.tobiasbielefeld.searchbar.SharedData.findBestLocaleMatchIndex;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;
import de.tobiasbielefeld.searchbar.R;


public class PreferenceLanguage extends CustomListPreference {

    Context context;

    public PreferenceLanguage(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public PreferenceLanguage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PreferenceLanguage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected int getCurrentValue() {
        String[] availableLanguages = context.getResources().getStringArray(R.array.pref_language_values);
        String systemLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        return findBestLocaleMatchIndex(availableLanguages, systemLanguage);
    }

    @Override
    public int getTitleArrayId() {
        return R.array.pref_language_titles;
    }

}
