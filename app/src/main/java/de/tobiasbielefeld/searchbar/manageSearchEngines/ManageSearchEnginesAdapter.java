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
    private ManageSearchEnginesViewHolder selected;
    private final ManageSearchEnginesActions actionHandlers;

    public ManageSearchEnginesAdapter(List<SearchEngine> items, ManageSearchEnginesActions actionHandlers) {
        this.items = items;
        this.actionHandlers = actionHandlers;
    }

    @NonNull
    @Override
    public ManageSearchEnginesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_manage_search_engines_item, parent, false);
        return new ManageSearchEnginesViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageSearchEnginesViewHolder holder, int position) {
        SearchEngine engine = items.get(position);
        holder.searchEngine = engine;
        holder.imageView.setImageBitmap(byteArrayToBitmap(engine.icon));
        holder.textViewName.setText(engine.label);
        holder.textViewUri.setText(engine.uri);
        holder.itemView.setOnClickListener((View view) -> {
            if (holder == selected || actionHandlers.isLoading()) {
                return;
            }

            if (selected != null) {
                selected.hideButtonsFromListItem();
            }

            holder.showButtonsFromListItem();
            selected = holder;
        });

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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ManageSearchEnginesViewHolder getSelectedItem() {
        return selected;
    }

    public List<SearchEngine> getItems() {
        return items;
    }

    public void moveScrollContainer(int itemBottom) {
        actionHandlers.moveScrollContainer(itemBottom);
    }

    public void moveItem(int fromPosition, int toPosition) {
        SearchEngine item = items.remove(fromPosition);
        items.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void removeItem(SearchEngine engine) {
        int position = items.indexOf(engine);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(SearchEngine oldEngine, SearchEngine newEngine) {
        int position = items.indexOf(oldEngine);

        if (position == -1) {
            items.add(newEngine);
            notifyItemInserted(items.size());
        } else {
            items.set(position, newEngine);
            notifyItemChanged(position);
        }
    }
}
