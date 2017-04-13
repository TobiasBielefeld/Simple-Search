/*
 * Copyright (C) 2017  Tobias Bielefeld
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

package de.tobiasbielefeld.searchbar;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import de.tobiasbielefeld.searchbar.helper.LocaleChanger;

/**
 * Application class to load custom locales
 */

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        LocaleChanger.setDefaultLocale(Locale.getDefault());
        super.attachBaseContext(LocaleChanger.onAttach(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.setLocale(this);
    }
}