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

import static androidx.core.content.ContextCompat.getSystemService;
import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import de.tobiasbielefeld.searchbar.R;

/**
 * Dialog to delete, share or copy a record entry
 */

public class DialogManageRecord extends DialogFragment {

    String text;

    public void setText(String text){
        this.text = text;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(text)
                .setItems(R.array.dialog_manage_record, (dialog, which) -> {
                    // "which" argument contains index of selected item
                    switch (which) {
                        case 0:
                            shareText();
                            break;
                        case 1:
                            copyText();
                            break;
                        case 2:
                            deleteText();
                            break;
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //cancel
                });

        return builder.create();
    }

    private void deleteText() {
        dismissAllowingStateLoss();

        DialogDeleteEntry dialog = new DialogDeleteEntry();
        dialog.setText(text);
        dialog.show(getParentFragmentManager(), "dialog");
    }

    private void copyText() {
        ClipboardManager clipboard = getSystemService(requireContext(), ClipboardManager.class);
        ClipData clip = ClipData.newPlainText("search text", text);

        if (clipboard == null) {
            showToast(getString(R.string.dialog_manage_record_clipboard_error), requireContext());
        } else {
            clipboard.setPrimaryClip(clip);
            showToast(getString(R.string.dialog_manage_record_copied_to_clipboard), requireContext());
        }
    }

    private void shareText() {
        dismissAllowingStateLoss();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share via");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requireContext().startActivity(shareIntent);
    }
}
