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

package de.tobiasbielefeld.searchbar.classes;

import static de.tobiasbielefeld.searchbar.SharedData.byteArrayToBitmap;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedSearchEngineLabel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.models.SearchEngine;

public class CustomMenuAdapter extends ArrayAdapter<SearchEngine> {

    public CustomMenuAdapter(Context context, List<SearchEngine> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView != null ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.search_engine_select_menu_item, parent, false);

        SearchEngine item = getItem(position);
        String selectedLabel = getSavedSearchEngineLabel();

        ImageView icon = view.findViewById(R.id.item_icon);
        TextView text = view.findViewById(R.id.item_text);

        icon.setImageBitmap(byteArrayToBitmap(item.icon));
        text.setText(item.label);
        view.setBackgroundResource(Objects.equals(item.label, selectedLabel) ? R.drawable.dialog_highlight : android.R.color.transparent);

        return view;
    }
}
