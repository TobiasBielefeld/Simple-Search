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

package de.tobiasbielefeld.searchbar.ui.settings;

import static de.tobiasbielefeld.searchbar.SharedData.getSavedToolbarColor;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceViewHolder;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomDialogPreference;



public class PreferenceToolbarColor extends CustomDialogPreference {

    public PreferenceToolbarColor(@NonNull Context context) {
        super(context);
    }

    public PreferenceToolbarColor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceToolbarColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        int color = getSavedToolbarColor();
        View image = holder.findViewById(R.id.widget_layout_color_imageView);
        Drawable background = image.getBackground().mutate();

        if (background instanceof ShapeDrawable shapeDrawable) {
            shapeDrawable.getPaint().setColor(color);
        } else if (background instanceof GradientDrawable gradientDrawable) {
            gradientDrawable.setColor(color);
        } else if (background instanceof ColorDrawable colorDrawable) {
            colorDrawable.setColor(color);
        }

        super.onBindViewHolder(holder);
    }

    @Override
    public void updateSummary(boolean positiveResult) {
        int color = getSavedToolbarColor();
        setSummary(String.format("#%06X", (0xFFFFFF & color)));  //show as hex string, but without the opacity part at the beginning
    }

    @Override
    protected int getDialogResourceId() {
        return R.layout.dialog_text_color;
    }
}
