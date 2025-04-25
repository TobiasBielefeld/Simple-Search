/*
 * Copyright (C) 2025  Tobias Bielefeld
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

import static de.tobiasbielefeld.searchbar.SharedData.getSavedBoolean;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedBoolean;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.SearchEngineItem;
import de.tobiasbielefeld.searchbar.helper.SearchEngines;

public class DialogShowSearchEngines extends CustomPreferenceDialogFragmentCompat  {

    private List<AppCompatCheckBox> checkBoxes;
    private List<Integer> selectedCheckboxes;
    private boolean loadedBundleState = false;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Integer> selected = new ArrayList<>();
        for (AppCompatCheckBox checkbox: checkBoxes) {
            selected.add(checkbox.isChecked() ? 1 : 0);
        }
        outState.putIntegerArrayList("selectedCheckboxes", selected);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedCheckboxes = savedInstanceState.getIntegerArrayList("selectedCheckboxes");
            loadedBundleState = true;
        }
    }

    protected void onClickOkay() {
        List<SearchEngineItem> searchEngineItems = SearchEngines.get(getResources()).items();

        for (int i = 0; i < checkBoxes.size(); i++) {
            putSavedBoolean(searchEngineItems.get(i).key() + "_shown", checkBoxes.get(i).isChecked());
        }
    }

    @Override
    @SuppressLint("RestrictedApi")
    protected void onBindView(View parent) {
        LinearLayout container = parent.findViewById(R.id.dialog_show_search_engines_container);
        List<SearchEngineItem> searchEngineItems = SearchEngines.get(getResources()).items();
        checkBoxes = new ArrayList<>();
        container.removeAllViews();

        for (int i = 0; i < searchEngineItems.size(); i++) {
            AppCompatCheckBox box = new AppCompatCheckBox(requireContext());
            box.setSupportButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent)));
            box.setText(searchEngineItems.get(i).label());

            checkBoxes.add(box);
            container.addView(box);

            if (loadedBundleState) {
                box.setChecked(selectedCheckboxes.get(i) == 1);
            } else if (getSavedBoolean(searchEngineItems.get(i).key() + "_shown", true)) {
                box.setChecked(true);
            }
        }
    }
}
