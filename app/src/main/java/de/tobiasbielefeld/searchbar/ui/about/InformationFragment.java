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

import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Locale;

import de.tobiasbielefeld.searchbar.BuildConfig;
import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.FragmentWithBottomSpacing;

/**
 * Shows some info about my app
 */

public class InformationFragment extends FragmentWithBottomSpacing {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_about_tab1, container, false);

        TextView textViewBuildDate = view.findViewById(R.id.aboutTextViewBuild);                     //build date
        TextView textViewAppVersion = view.findViewById(R.id.aboutTextViewVersion);                  //app version
        TextView textViewGitHubLink = view.findViewById(R.id.aboutTextViewGitHubLink);               //link for the gitHub repo
        TextView textViewLicenseLink = view.findViewById(R.id.aboutTextViewLicenseLink);
        TextView textFrenchContributors = view.findViewById(R.id.about_french_contributors);
        TextView textFurtherContributors1 = view.findViewById(R.id.about_further_contributors_1);
        TextView textFurtherContributors2 = view.findViewById(R.id.about_further_contributors_2);
        TextView textFurtherContributors3 = view.findViewById(R.id.about_further_contributors_3);

        String buildDate = DateFormat.getDateInstance().format(BuildConfig.TIMESTAMP);                          //get the build date in locale time format

        //update the textViews
        textViewAppVersion.setText(stringFormat(BuildConfig.VERSION_NAME));
        textViewBuildDate.setText(stringFormat(buildDate));

        //enable the hyperlink clicks
        TextView[] textViews = new TextView[]{textViewGitHubLink,textViewLicenseLink,
                textFrenchContributors, textFurtherContributors1, textFurtherContributors2, textFurtherContributors3
        };

        for (TextView textView : textViews){
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setLinkTextColor(getResources().getColor(R.color.colorTextLink));
        }

        return view;
    }

    public static String stringFormat(String text){
        return String.format(Locale.getDefault(),"%s", text);
    }
}