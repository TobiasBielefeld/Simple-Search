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

package de.tobiasbielefeld.searchbar.ui.settings.helpers;

import static de.tobiasbielefeld.searchbar.helper.InsetHelper.*;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.dialogs.CustomPreferenceDialogFragmentCompat;

public class CustomPreferenceFragmentCompat extends PreferenceFragmentCompat {

    List<DialogInfo> dialogs = new ArrayList<>();

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        // empty
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyInset(getListView(), InsetLocation.BOTTOM, InsetMode.PADDING);
    }

    // Bind the dialog information to this fragment and also perform the initial update of the preference
    protected void bindDialog(CustomDialogPreference pref, Class<? extends CustomPreferenceDialogFragmentCompat> dialogClass) {
        dialogs.add(new DialogInfo(pref, dialogClass));
        pref.updateSummary(false);
    }

    @Override
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        boolean found = false;

        for (DialogInfo info: dialogs) {
            // if we have info for the given preference
            if (info.pref == preference) {
                try {
                    CustomPreferenceDialogFragmentCompat dialog = info.dialogClass.newInstance();
                    showDialog(dialog, preference);
                    found = true;
                } catch (IllegalAccessException | java.lang.InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (!found) {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void showDialog(PreferenceDialogFragmentCompat dialog, Preference preference) {
        Bundle b = new Bundle();
        b.putString("key", preference.getKey());
        dialog.setArguments(b);
        dialog.setTargetFragment(this, 0);
        dialog.show(getParentFragmentManager(), "dialog");
    }
}
