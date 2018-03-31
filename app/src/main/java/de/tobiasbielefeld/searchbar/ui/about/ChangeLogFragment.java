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
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;

/*
 * Shows the changelog, which is simply loaded from a webView. The About activity disables recreation
 * after orientation change, so don't need to handle that.
 */

public class ChangeLogFragment extends Fragment {

    private static int MAX_LINES_PER_VERSION = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_about_tab3, container, false);

        LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.changelog_container);

        String[] titles = new String[]{"1.1.3", "1.1.2", "1.1.1", "1.1", "1.0"};

        for (int i = 0; i < titles.length; i++) {
            CardView card = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.changelog_card_view, null);
            TextView title = (TextView) card.findViewById(R.id.changelog_card_view_title);
            TextView description = (TextView) card.findViewById(R.id.changelog_card_view_text);

            title.setText(titles[i]);
            description.setText(createText(titles.length - i));

            layoutContainer.addView(card);
        }

        return view;
    }


    private CharSequence createText(int pos) {

        List<CharSequence> stringList = new ArrayList<>(MAX_LINES_PER_VERSION);

        //load the lines from the changelog separately
        for (int i = 1; i <= MAX_LINES_PER_VERSION; i++) {

            int ID = getResources().getIdentifier(
                    "changelog_" + Integer.toString(pos) + "_" + Integer.toString(i),
                    "string", getActivity().getPackageName());

            if (ID != 0) {
                stringList.add(getString(ID));
            } else {
                break;
            }
        }

        SpannableString spanns[] = new SpannableString[stringList.size()];

        //apply the bullet characters
        for (int i = 0; i < stringList.size(); i++) {
            spanns[i] = new SpannableString(stringList.get(i));
            spanns[i].setSpan(new BulletSpan(15), 0, stringList.get(i).length(), 0);
        }

        return TextUtils.concat(spanns);
    }
}