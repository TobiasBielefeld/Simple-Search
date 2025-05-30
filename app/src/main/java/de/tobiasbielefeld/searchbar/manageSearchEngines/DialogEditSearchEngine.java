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
import static de.tobiasbielefeld.searchbar.SharedData.database;
import static de.tobiasbielefeld.searchbar.SharedData.drawableToByteArray;
import static de.tobiasbielefeld.searchbar.SharedData.getSelectedSearchEngine;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedSearchEngineLabel;
import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.helper.FaviconLoader;
import de.tobiasbielefeld.searchbar.helper.LoadingTaskHelper;
import de.tobiasbielefeld.searchbar.models.SearchEngine;

public class DialogEditSearchEngine {

    private final Context context;
    private final SearchEngine editEntry;
    private RelativeLayout loadingLayout;
    private ImageView favicon;
    private EditText inputName, inputUri;
    private ProgressBar loadingSpinner;
    private final ManageSearchEnginesAdapter adapter;

    public DialogEditSearchEngine(Context context, ManageSearchEnginesAdapter adapter, SearchEngine editEngine) {
        this.context = context;
        this.adapter = adapter;
        this.editEntry = editEngine;
    }

    public void show() {
        View customView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_search_engine, null);
        onBindView(customView);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_edit_search_engine_title))
                .setPositiveButton(context.getString(R.string.confirm), null)
                .setNegativeButton(android.R.string.cancel, null)
                .setView(customView)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onClickOkay(dialog));
        });

        dialog.show();
    }

    private void onBindView(View view) {
        loadingLayout = view.findViewById(R.id.layout_loading);
        favicon = view.findViewById(R.id.favicon);
        inputName = view.findViewById(R.id.search_name);
        inputUri = view.findViewById(R.id.search_uri);
        loadingSpinner = view.findViewById(R.id.loading_spinner);

        MaterialButton buttonRefresh = view.findViewById(R.id.button_refresh);
        buttonRefresh.setOnClickListener((View v) -> FaviconLoader.loadFavicon(context, getUri(), favicon, loadingSpinner));

        if (editEntry != null) {
            inputName.setText(editEntry.label);
            inputUri.setText(editEntry.uri);
            favicon.setImageBitmap(byteArrayToBitmap(editEntry.icon));
        }
    }

    private void onClickOkay(AlertDialog dialog) {
        String newUri = getUri();
        String newLabel = inputName.getText().toString();

        if (editEntry == null && labelAlreadyExists(adapter.getItems(), newLabel)) {
            showToast(context.getString(R.string.dialog_edit_search_engine_name_error), context);
            return;
        } else if (!newUri.contains("%s")) {
            showToast(context.getString(R.string.string_doesnt_contain_placeholder), context);
            return;
        }

        new LoadingTaskHelper(loadingLayout,
                () -> saveSearchEngine(newLabel, newUri),
                () ->  {
                    showToast(context.getString(R.string.dialog_edit_search_engine_name_success), context);
                    dialog.dismiss();
                }
        ).execute();
    }

    private void saveSearchEngine(String newLabel, String newUri) {
        SearchEngine newEngine = new SearchEngine(newLabel, newUri, false, drawableToByteArray(favicon.getDrawable()));
        SearchEngine selectedSearchEngine = getSelectedSearchEngine(adapter.getItems());

        database.withTransaction(inputName, context, () -> {
            // delete existing entry when we change the label
            if (editEntry != null && !newLabel.equals(editEntry.label)) {
                database.deleteSearchEngine(editEntry);

                // update selected search engine if necessary
                if (editEntry.label.equals(selectedSearchEngine.label)) {
                    putSavedSearchEngineLabel(newLabel);
                }
            }

            database.putSearchEngine(newEngine);
        });
    }

    private String getUri() {
        String uri = inputUri.getText().toString();

        if (!uri.startsWith("https://") && !uri.startsWith("http://")) {
            uri = "https://" + uri;
        }

        return uri;
    }

    private boolean labelAlreadyExists(List<SearchEngine> engines, String label) {
        for (SearchEngine engine: engines) {
            if (engine.label.equals(label)) {
                return true;
            }
        }

        return false;
    }
}
