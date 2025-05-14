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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.FragmentWithBottomSpacing;

import static de.tobiasbielefeld.searchbar.SharedData.createBulletParagraph;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/*
 * Shows the changelog, which is simply loaded from a webView. The About activity disables recreation
 * after orientation change, so don't need to handle that.
 */

public class ChangeLogFragment extends FragmentWithBottomSpacing {

    private final static int MAX_LINES_PER_VERSION = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_about_tab3, container, false);

        LinearLayout layoutContainer = view.findViewById(R.id.changelog_container);

        String[] titles = new String[]{
                "2.3", "2.2", "2.1", "2.0.2", "2.0.1", "2.0", "1.2", "1.1.4",
                "1.1.3", "1.1.2", "1.1.1", "1.1", "1.0"
        };

        for (int i = 0; i < titles.length; i++) {
            CardView card = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.changelog_card_view, null);
            TextView title = card.findViewById(R.id.changelog_card_view_title);
            TextView description = card.findViewById(R.id.changelog_card_view_text);

            title.setText(titles[i]);
            description.setText(createText(titles.length - i));

            layoutContainer.addView(card);
        }

        return view;
    }


    private CharSequence createText(int pos){

        List<CharSequence> stringList = new ArrayList<>(MAX_LINES_PER_VERSION);

        //load the lines from the changelog separately
        for (int i = 1; i <= MAX_LINES_PER_VERSION; i++) {

            int ID = getResources().getIdentifier("changelog_" + pos + "_" + i, "string", getActivity().getPackageName());

            if (ID != 0) {
                stringList.add(getString(ID));
            } else {
                break;
            }
        }

        return TextUtils.concat(createBulletParagraph(stringList.toArray(new CharSequence[0])));
    }
}