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

import de.tobiasbielefeld.searchbar.dialogs.CustomPreferenceDialogFragmentCompat;

public class DialogInfo {
    public CustomDialogPreference pref;
    public Class<? extends CustomPreferenceDialogFragmentCompat> dialogClass;

    public DialogInfo(CustomDialogPreference pref, Class<? extends CustomPreferenceDialogFragmentCompat> dialogClass) {
        this.pref = pref;
        this.dialogClass = dialogClass;
    }
}
