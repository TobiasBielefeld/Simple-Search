package de.tobiasbielefeld.searchbar.ui.settings.helpers;

import de.tobiasbielefeld.searchbar.dialogs.CustomPreferenceDialogFragmentCompat;

public class DialogInfo {
    public CustomDialogPreference pref;
    public Class<? extends CustomPreferenceDialogFragmentCompat> dialogClass;

    public DialogInfo(CustomDialogPreference pref, Class<? extends CustomPreferenceDialogFragmentCompat> dialogClass) {
        this.pref = pref;
        this.dialogClass = dialogClass;
    }
}
