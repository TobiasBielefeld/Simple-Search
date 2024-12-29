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

package de.tobiasbielefeld.searchbar.helper;

import static de.tobiasbielefeld.searchbar.SharedData.getSavedEdgeToEdgeDisplayMode;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InsetHelper {

    public static boolean usesEdgelessMode() {
        // only use edgeless mode on Android 11 and up, because
        // the windowInsets API doesn't return useful values for older versions
        return getSavedEdgeToEdgeDisplayMode() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    public static void applyEdgelessMode(ComponentActivity activity) {
        if (usesEdgelessMode()) {
            EdgeToEdge.enable(activity);
        }
    }

    public static void applyInsetsForActivity(View leftSpacer, View rightSpacer, View topSpacer) {
        if (usesEdgelessMode()) {
            applyInset(leftSpacer, InsetLocation.LEFT, InsetMode.SET);
            applyInset(rightSpacer, InsetLocation.RIGHT, InsetMode.SET);
            applyInset(topSpacer, InsetLocation.TOP, InsetMode.SET);
        }
    }

    public static void applyInset(View view, InsetLocation location, InsetMode mode) {
        if (usesEdgelessMode()) {
            ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(
                        WindowInsetsCompat.Type.navigationBars() |
                                WindowInsetsCompat.Type.systemBars() |
                                WindowInsetsCompat.Type.displayCutout() |
                                WindowInsetsCompat.Type.ime()
                );

                if (mode == InsetMode.PADDING) {
                    switch (location) {
                        case LEFT -> v.setPadding(insets.left, 0, 0, 0);
                        case RIGHT -> v.setPadding(0, 0, insets.right, 0);
                        case TOP -> v.setPadding(0, insets.top, 0, 0);
                        case BOTTOM -> v.setPadding(0, 0, 0, insets.bottom);
                    }
                } else if (mode == InsetMode.SET) {
                    ViewGroup.LayoutParams params = v.getLayoutParams();
                    switch (location) {
                        case LEFT -> params.width = insets.left;
                        case RIGHT -> params.width = insets.right;
                        case TOP -> params.height = insets.top;
                        case BOTTOM -> params.height = insets.bottom;
                    }
                    v.setLayoutParams(params);
                }
                return WindowInsetsCompat.CONSUMED;
            });
        }
    }

    public enum InsetLocation {LEFT, RIGHT, TOP, BOTTOM}

    public enum InsetMode {PADDING, SET}
}
