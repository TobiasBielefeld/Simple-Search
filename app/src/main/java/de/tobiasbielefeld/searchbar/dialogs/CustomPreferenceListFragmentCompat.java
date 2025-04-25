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

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;

public abstract class CustomPreferenceListFragmentCompat extends CustomPreferenceDialogFragmentCompat {

    private RadioGroup radioGroup;
    private boolean loadedBundleState = false;
    protected int selectedIndex = -1;

    protected abstract void selectionChanged();

    protected abstract int getSelectedIndex();
    protected void onBindView(View view) {
        if (getPreference() instanceof CustomListPreference) {
            String[] values = ((CustomListPreference) getPreference()).getTitles();
            radioGroup = view.findViewById(R.id.radio_group);
            int height = (int) getResources().getDimension(R.dimen.dialog_radio_button_height);

            if (!loadedBundleState) {
                selectedIndex = getSelectedIndex();
            }

            for (int i = 0; i < values.length; i++) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(values[i]);
                radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioButton.setHeight(height);
                radioButton.setOnClickListener(this::onClick);
                radioGroup.addView(radioButton);

                if (i == selectedIndex) {
                    radioGroup.check(radioButton.getId());
                }
            }
        }
    }

    private void onClick(View view) {
        selectedIndex = radioGroup.indexOfChild(view);
        selectionChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt("selectedIndex");
            loadedBundleState = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedIndex", selectedIndex);
    }
}
