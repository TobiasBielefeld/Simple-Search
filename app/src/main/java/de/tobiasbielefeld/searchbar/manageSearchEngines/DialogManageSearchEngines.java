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

import static de.tobiasbielefeld.searchbar.SharedData.database;
import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.dialogs.CustomPreferenceDialogFragmentCompat;
import de.tobiasbielefeld.searchbar.helper.AfterViewTreeObserved;
import de.tobiasbielefeld.searchbar.helper.LoadingTaskHelper;
import de.tobiasbielefeld.searchbar.models.SearchEngine;


public class DialogManageSearchEngines extends CustomPreferenceDialogFragmentCompat implements ManageSearchEnginesActions {
    private final AtomicBoolean isLoading = new AtomicBoolean(false);
    private RecyclerView layoutContainer;
    private RelativeLayout layoutLoading;
    private ScrollView scrollView;
    ManageSearchEnginesAdapter listAdapter;
    private List<SearchEngine> engines;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    protected void onBindView(View view) {
        layoutContainer = view.findViewById(R.id.recycler_view_items);
        scrollView = view.findViewById(R.id.scrollview_container);
        layoutLoading = view.findViewById(R.id.layout_loading);

        startLoading();

        AfterViewTreeObserved.run(layoutLoading, () ->
                new LoadingTaskHelper(layoutLoading,
                        () -> engines = database.getSearchEngines(),
                        () -> {
                            listAdapter = new ManageSearchEnginesAdapter(engines, this);
                            layoutContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
                            layoutContainer.setAdapter(listAdapter);
                            stopLoading();
                        }
                ).execute()
        );
    }

    @Override
    protected void onClickOkay(AlertDialog dialog) {
        if (engines != null) {
            DialogEditSearchEngine editDialog = new DialogEditSearchEngine(getActivity(), listAdapter, null);
            editDialog.show();
            hideDialog();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void deleteEntry(SearchEngine entry) {
        startLoading();
        new LoadingTaskHelper(layoutLoading,
                () -> database.deleteSearchEngine(entry),
                () -> {
                    listAdapter.removeItem(entry);
                    showToast(getString(R.string.settings_manage_search_engines_deleted), getContext());
                    stopLoading();
                }
        ).execute();
    }

    @Override
    public void moveScrollContainer(int itemBottom) {
        final int scrollHeight = scrollView.getHeight();

        if (itemBottom > (scrollView.getScrollY() + scrollHeight)) {
             scrollView.post(() -> scrollView.smoothScrollTo(0, itemBottom - scrollHeight));
        }
    }

    public boolean isLoading() {
        return isLoading.get();
    }

    public void hideDialog() {
        dismiss();
    }

    private void startLoading() {
        setCancelable(false);
        isLoading.set(true);
    }

    private void stopLoading() {
        setCancelable(true);
        isLoading.set(false);
    }
}
