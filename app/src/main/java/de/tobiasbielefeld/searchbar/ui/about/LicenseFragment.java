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

package de.tobiasbielefeld.searchbar.ui.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.FragmentWithBottomSpacing;

/*
 * Shows the GPL License, which is simply loaded from a webView. The About activity disables recreation
 * after orientation change, so don't need to handle that.
 */

public class LicenseFragment extends FragmentWithBottomSpacing {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_about_tab2, container, false);

        TextView textMaterialIconsLicense = view.findViewById(R.id.about_license_material_icons);
        TextView textAndroidSupportLicense = view.findViewById(R.id.about_license_android_support_libraries);
        TextView textCustomColorPickerLicense = view.findViewById(R.id.about_license_custom_color_picker);
        TextView textJsoupLicense = view.findViewById(R.id.about_license_jsoup);
        TextView textGsonLicense = view.findViewById(R.id.about_license_gson);

        TextView[] textViews = new TextView[]{
                textMaterialIconsLicense, textAndroidSupportLicense,
                textCustomColorPickerLicense, textJsoupLicense, textGsonLicense
        };

        for (TextView textView : textViews){
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setLinkTextColor(getResources().getColor(R.color.colorTextLink));
        }

        return view;
    }
}