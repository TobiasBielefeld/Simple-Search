/*
 * Copyright (C) 2016  Tobias Bielefeld
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

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import de.tobiasbielefeld.searchbar.R;


public abstract class CustomListPreference extends CustomDialogPreference {

    Context context;

    public CustomListPreference(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void updateSummary(boolean positiveResult) {
        int index = getCurrentValue();
        setSummary(getTitles()[index]);
    }

    @Override
    public int getDialogResourceId() {
        return R.layout.dialog_radio_list_template;
    }

    protected abstract int getCurrentValue();

    public abstract int getTitleArrayId();

    public String[] getTitles() {
        return context.getResources().getStringArray(getTitleArrayId());
    }
}
