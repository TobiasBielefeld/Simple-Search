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

package de.tobiasbielefeld.searchbar.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import de.tobiasbielefeld.searchbar.R;

import static de.tobiasbielefeld.searchbar.SharedData.records;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Simply delete all records when pressing the confirm button
 */

public class DialogDeleteAll extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_delete_all_records_text)
                .setPositiveButton(R.string.confirm, (dialog, id) -> records.deleteAll())
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //cancel
                });

        return builder.create();
    }
}
