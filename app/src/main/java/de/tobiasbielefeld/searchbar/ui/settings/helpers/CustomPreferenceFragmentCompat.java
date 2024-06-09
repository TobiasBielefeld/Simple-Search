package de.tobiasbielefeld.searchbar.ui.settings.helpers;

import android.os.Bundle;

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
