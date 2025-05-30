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

package de.tobiasbielefeld.searchbar.classes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static de.tobiasbielefeld.searchbar.SharedData.*;
import static de.tobiasbielefeld.searchbar.helper.InsetHelper.applyEdgelessMode;

import java.util.Locale;

public abstract class CustomAppCompatActivity extends AppCompatActivity {

    private Locale lastKnownLocale;
    private int lastKnownTheme;
    private int lastKnownOrientation;

    protected Toolbar toolbar; // needs to be set in class instantiation
    protected View systemTopSpacer; // needs to be set in class instantiation

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        reinitializeData(this);

        lastKnownLocale = Locale.getDefault();

        lastKnownTheme = getSavedTheme();
        changeTheme(lastKnownTheme);

        lastKnownOrientation = getSavedOrientation();
        setOrientation(lastKnownOrientation);

        applyEdgelessMode(this);
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        showOrHideStatusBar(getWindow(), getSavedStatusBar());
        changeToolbarColor();

        Locale newLocale = Locale.getDefault();
        if (!lastKnownLocale.getLanguage().equals(newLocale.getLanguage())) {
            lastKnownLocale = newLocale;
            recreate();
        }

        int newTheme = getSavedTheme();
        if (lastKnownTheme != newTheme) {
            lastKnownTheme = newTheme;
            changeTheme(newTheme);
        }

        int newOrientation = getSavedOrientation();
        if (lastKnownOrientation != newOrientation) {
            lastKnownOrientation = newOrientation;
            setOrientation(lastKnownOrientation);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeTheme(int theme) {
        switch (theme) {
            case 0 -> getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            case 1 -> getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            case 2 -> getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case 1 -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            case 2 -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            case 3 -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            case 4 -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    public void changeToolbarColor() {
        int savedColor = getSavedToolbarColor();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(savedColor);
        toolbar.setBackgroundColor(savedColor);
        systemTopSpacer.setBackgroundColor(savedColor);
    }
}
