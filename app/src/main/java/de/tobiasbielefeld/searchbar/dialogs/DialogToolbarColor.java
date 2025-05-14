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

import static de.tobiasbielefeld.searchbar.SharedData.getSavedToolbarColor;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedToolbarColor;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Dialog for changing the toolbar color. It uses a custom layout, so I can dynamically update
 * the widget icon of the preference. The custom color chooser uses this library: https://github.com/yukuku/ambilwarna
 */

public class DialogToolbarColor extends CustomPreferenceDialogFragmentCompat implements View.OnClickListener {
    private List<View> linearLayouts;
    private List<Integer> allColors;
    private Activity activity;

    @Override
    protected void onBindView(View view) {
        activity = getActivity();

        allColors = Arrays.asList(
            getResources().getColor(R.color.colorPrimary),
            getResources().getColor(R.color.settings_color_red),
            getResources().getColor(R.color.settings_color_pink),
            getResources().getColor(R.color.settings_color_purple),
            getResources().getColor(R.color.settings_color_deep_purple),
            getResources().getColor(R.color.settings_color_indigo),
            getResources().getColor(R.color.settings_color_blue),
            getResources().getColor(R.color.settings_color_light_blue),
            getResources().getColor(R.color.settings_color_cyan),
            getResources().getColor(R.color.settings_color_teal),
            getResources().getColor(R.color.settings_color_green),
            getResources().getColor(R.color.settings_color_light_green),
            getResources().getColor(R.color.settings_color_lime),
            getResources().getColor(R.color.settings_color_yellow),
            getResources().getColor(R.color.settings_color_amber),
            getResources().getColor(R.color.settings_color_orange),
            getResources().getColor(R.color.settings_color_deep_orange),
            getResources().getColor(R.color.settings_color_brown),
            getResources().getColor(R.color.settings_color_grey),
            getResources().getColor(R.color.settings_color_blue_grey)
        );

        linearLayouts = Arrays.asList(
            view.findViewById(R.id.dialogBackgroundColorPrimary),
            view.findViewById(R.id.dialogBackgroundColorRed),
            view.findViewById(R.id.dialogBackgroundColorPink),
            view.findViewById(R.id.dialogBackgroundColorPurple),
            view.findViewById(R.id.dialogBackgroundColorDeepPurple),
            view.findViewById(R.id.dialogBackgroundColorIndigo),
            view.findViewById(R.id.dialogBackgroundColorBlue),
            view.findViewById(R.id.dialogBackgroundColorLightBlue),
            view.findViewById(R.id.dialogBackgroundColorCyan),
            view.findViewById(R.id.dialogBackgroundColorTeal),
            view.findViewById(R.id.dialogBackgroundColorGreen),
            view.findViewById(R.id.dialogBackgroundColorLightGreen),
            view.findViewById(R.id.dialogBackgroundColorLime),
            view.findViewById(R.id.dialogBackgroundColorYellow),
            view.findViewById(R.id.dialogBackgroundColorAmber),
            view.findViewById(R.id.dialogBackgroundColorOrange),
            view.findViewById(R.id.dialogBackgroundColorDeepOrange),
            view.findViewById(R.id.dialogBackgroundColorBrown),
            view.findViewById(R.id.dialogBackgroundColorGrey),
            view.findViewById(R.id.dialogBackgroundColorBlueGrey)
        );

        for (View linearLayout : linearLayouts) {
            linearLayout.setOnClickListener(this);
        }

        updateDialog();
    }

    @Override
    protected void onClickOkay(AlertDialog dialog) {
        showCustomColorDialog();
        dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        int selectedIndex = linearLayouts.indexOf(view);
        putSavedToolbarColor(allColors.get(selectedIndex));
        updateToolbarColor();
        dismiss();
    }

    private void showCustomColorDialog() {
        int colorValue = getSavedToolbarColor();

        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), colorValue, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                putSavedToolbarColor(color);
                updateToolbarColor();
                DialogToolbarColor.super.onDialogClosed(true);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        dialog.show();
    }

    private void updateDialog() {
        int color = getSavedToolbarColor();

        for (int i = 0; i < allColors.size(); i++) {
            if (color == allColors.get(i)) {
                linearLayouts.get(i).setBackgroundResource(R.drawable.settings_highlight_round);
                break;
            }
        }
    }

    private void updateToolbarColor() {
        if (activity instanceof CustomAppCompatActivity customAppCompatActivity) {
            customAppCompatActivity.changeToolbarColor();
        }
    }
}
