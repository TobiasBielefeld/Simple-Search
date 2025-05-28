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

package de.tobiasbielefeld.searchbar.manageSearchEngines;

import static de.tobiasbielefeld.searchbar.SharedData.viewCollapse;
import static de.tobiasbielefeld.searchbar.SharedData.viewExpand;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.models.SearchEngine;

public class ManageSearchEnginesViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    LinearLayout layoutButtons;
    TextView textViewName, textViewUri;
    Button buttonEdit, buttonDelete;
    Context context;
    SearchEngine searchEngine;
    ManageSearchEnginesAdapter adapter;
    private final TypedValue typedValue = new TypedValue();

    public ManageSearchEnginesViewHolder(View item, ManageSearchEnginesAdapter adapter) {
        super(item);
        this.adapter = adapter;
        context = item.getContext();
        imageView = item.findViewById(R.id.image_view);
        textViewName = item.findViewById(R.id.dialog_manage_search_engines_name);
        textViewUri = item.findViewById(R.id.dialog_manage_search_engines_uri);
        layoutButtons = item.findViewById(R.id.dialog_manage_search_engines_layout_buttons);
        buttonEdit = item.findViewById(R.id.dialog_manage_search_engines_button_edit);
        buttonDelete = item.findViewById(R.id.dialog_manage_search_engines_button_delete);
        item.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
    }

    public void setHighlighted(boolean highlighted) {
        boolean shouldBeHighlighted = highlighted || adapter.getSelectedItem() == this;
        itemView.setBackgroundResource(shouldBeHighlighted ? R.drawable.dialog_highlight : typedValue.resourceId);
    }

    public void showButtonsFromListItem() {
        itemView.setBackgroundResource(R.drawable.dialog_highlight);

        if (adapter.getItemCount() == 1) {
            buttonDelete.setVisibility(View.GONE); // cannot delete last entry
        }

        viewExpand(layoutButtons, () -> adapter.moveScrollContainer(itemView.getBottom()));
    }

    public void hideButtonsFromListItem() {
        itemView.setBackgroundResource(typedValue.resourceId);
        viewCollapse(layoutButtons, null);
    }
}