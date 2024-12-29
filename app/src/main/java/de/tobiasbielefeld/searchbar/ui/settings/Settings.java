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

package de.tobiasbielefeld.searchbar.ui.settings;

import static de.tobiasbielefeld.searchbar.helper.InsetHelper.*;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.databinding.ActivitySettingsBinding;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomPreferenceFragmentCompat;

public class Settings extends CustomAppCompatActivity {

    public static String FRAGMENT_TAG = "SETTINGS_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        CustomPreferenceFragmentCompat fragment = new FragmentSettings();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settings, fragment, FRAGMENT_TAG).commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        applyInsetsForActivity(findViewById(R.id.system_left_spacer), findViewById(R.id.system_right_spacer), findViewById(R.id.system_top_spacer));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           //only menu item is the back mButton in the action bar
        finish();                                                                                   //so finish this activity
        return true;
    }
}
