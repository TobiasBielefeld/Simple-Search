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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomDialogPreference;

public abstract class CustomPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private boolean customResult;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use custom alert dialog because the standard preference dialog has no way to access the onclick button methods
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setTitle(getPreference().getTitle());

        CharSequence positiveButtonText = getPreference().getPositiveButtonText();

        if (positiveButtonText != null && !positiveButtonText.equals("NULL")) {
            alertDialogBuilder.setPositiveButton(getPreference().getPositiveButtonText(), (DialogInterface dialog, int which) -> {
                customResult = true;
                onClickOkay();
            });
        }
        alertDialogBuilder.setNegativeButton(getPreference().getNegativeButtonText(), (DialogInterface dialog, int which) -> {
            customResult = false;
            onCancel();
            dismiss();
        });

        View customView = LayoutInflater.from(requireContext()).inflate(getPreference().getDialogLayoutResource(), null);
        onBindView(customView);

        alertDialogBuilder.setView(customView);
        return alertDialogBuilder.create();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (getPreference() instanceof CustomDialogPreference) {
            ((CustomDialogPreference) getPreference()).updateSummary(customResult);
        }
    }

    protected void onClickOkay() {}

    protected void onCancel() {}

    protected abstract void onBindView(View view);

}
