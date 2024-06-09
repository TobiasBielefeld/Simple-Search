package de.tobiasbielefeld.searchbar.dialogs;

import static de.tobiasbielefeld.searchbar.SharedData.DEFAULT_SEARCH_SELECTED_INDEX;
import static de.tobiasbielefeld.searchbar.SharedData.DEFAULT_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.PREF_CUSTOM_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.PREF_SEARCH_SELECTED_INDEX;
import static de.tobiasbielefeld.searchbar.SharedData.PREF_SEARCH_URL;
import static de.tobiasbielefeld.searchbar.SharedData.getSavedString;
import static de.tobiasbielefeld.searchbar.SharedData.putSavedString;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.tobiasbielefeld.searchbar.R;

public class DialogSearchEngines extends CustomPreferenceListFragmentCompat {

    private EditText customEditText;

    @Override
    protected void selectionChanged() {
        if (selectedIndex < 3) {
            showCustomSearchEngineDialog(selectedIndex);
        } else {
            String[] searchUris = getResources().getStringArray(R.array.search_engine_uris);
            putSavedString(PREF_SEARCH_SELECTED_INDEX, String.valueOf(selectedIndex));
            putSavedString(PREF_SEARCH_URL, searchUris[selectedIndex]);
            dismiss();
        }
    }

    @Override
    protected int getSelectedIndex() {
        return Integer.parseInt(getSavedString(PREF_SEARCH_SELECTED_INDEX, String.valueOf(DEFAULT_SEARCH_SELECTED_INDEX)));
    }

    private void showCustomSearchEngineDialog(final int which){
        final String prefKey = which > 0 ? PREF_CUSTOM_SEARCH_URL + which : PREF_CUSTOM_SEARCH_URL;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_search_engine_dialog, null);

        customEditText = v.findViewById(R.id.custom_search_url);
        String savedSearchUrl = getSavedString(PREF_SEARCH_URL, DEFAULT_SEARCH_URL);
        customEditText.setText(getSavedString(prefKey, savedSearchUrl));

        builder.setView(v);
        builder.setTitle(R.string.dialog_custom_search_engine_title)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    String text = customEditText.getText().toString();
                    putSavedString(PREF_SEARCH_URL, text);
                    putSavedString(prefKey, text);
                    putSavedString(PREF_SEARCH_SELECTED_INDEX, String.valueOf(selectedIndex));
                    selectedIndex = which;
                    dialog.dismiss();
                    dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    //cancel
                });

        builder.show();
    }
}
