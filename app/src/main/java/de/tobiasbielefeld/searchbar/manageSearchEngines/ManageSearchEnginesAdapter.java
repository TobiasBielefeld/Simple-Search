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

import static de.tobiasbielefeld.searchbar.SharedData.byteArrayToBitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.models.SearchEngine;

public class ManageSearchEnginesAdapter extends RecyclerView.Adapter<ManageSearchEnginesViewHolder> {
    private final List<SearchEngine> items;
    private int selectedIndex = -1;
    private final ManageSearchEnginesActions actionHandlers;

    public ManageSearchEnginesAdapter(List<SearchEngine> items, ManageSearchEnginesActions actionHandlers) {
        this.items = items;
        this.actionHandlers = actionHandlers;
    }

    @NonNull
    @Override
    public ManageSearchEnginesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_manage_search_engines_item, parent, false);
        return new ManageSearchEnginesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageSearchEnginesViewHolder holder, int position) {
        SearchEngine engine = items.get(position);
        holder.searchEngine = engine;
        holder.imageView.setImageBitmap(byteArrayToBitmap(engine.icon));
        holder.textViewName.setText(engine.label);
        holder.textViewUri.setText(engine.uri);
        holder.itemView.setOnClickListener((View view) -> onItemClick(position));

        holder.buttonEdit.setOnClickListener((View button) -> {
            if (!actionHandlers.isLoading()) {
                DialogEditSearchEngine dialog = new DialogEditSearchEngine(holder.context, this, engine);
                dialog.show();
                actionHandlers.hideDialog();
            }
        });

        holder.buttonDelete.setOnClickListener((View button) -> {
            if (!actionHandlers.isLoading() && items.size() > 1) {
                actionHandlers.deleteEntry(engine);
            }
        });

        holder.itemView.setBackgroundResource(selectedIndex == position ? R.drawable.dialog_highlight : 0);
        holder.layoutButtons.setVisibility(selectedIndex == position ? View.VISIBLE : View.GONE);
        holder.buttonDelete.setVisibility(getItemCount() == 1 ? View.GONE : View.VISIBLE); // cannot delete last entry
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<SearchEngine> getItems() {
        return items;
    }

    public void removeItem(SearchEngine engine) {
        int position = items.indexOf(engine);
        items.remove(position);
        notifyItemRemoved(position);
    }

    private void onItemClick(int newIndex) {
        if (newIndex == selectedIndex  || actionHandlers.isLoading()) {
            return;
        }

        if (items.get(newIndex) != null) {
            collapsePreviousItem(selectedIndex);
            notifyItemChanged(newIndex);
            selectedIndex = newIndex;
            actionHandlers.moveScrollContainer(newIndex);
        }
    }

    private void collapsePreviousItem(int prevIndex) {
        if (prevIndex != -1) {
            notifyItemChanged(prevIndex);
        }
    }
}
