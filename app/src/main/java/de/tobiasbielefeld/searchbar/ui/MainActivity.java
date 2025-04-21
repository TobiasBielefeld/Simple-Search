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

package de.tobiasbielefeld.searchbar.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.classes.CustomMenuAdapter;
import de.tobiasbielefeld.searchbar.classes.SearchEngineItem;
import de.tobiasbielefeld.searchbar.helper.Records;
import de.tobiasbielefeld.searchbar.helper.SearchEngines;
import de.tobiasbielefeld.searchbar.ui.about.AboutActivity;
import de.tobiasbielefeld.searchbar.ui.settings.Settings;

import static de.tobiasbielefeld.searchbar.SharedData.*;
import static de.tobiasbielefeld.searchbar.helper.InsetHelper.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class MainActivity extends CustomAppCompatActivity implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener {

    public EditText searchText;
    private ImageView clearButton, searchEngineSelectButton;
    private ActivityResultLauncher<Intent> startForResult;

    private SearchEngines searchEngines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup navbar with item select listener
        Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        searchText = findViewById(R.id.editTextSearch);
        clearButton = findViewById(R.id.imageButtonClear);
        searchEngineSelectButton = findViewById(R.id.imageButtonSelectSearchEngines);

        searchText.addTextChangedListener(this);
        searchText.setOnEditorActionListener(this);
        searchEngines = SearchEngines.get(getResources());

        records = new Records(this, findViewById(R.id.record_list_container));

        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);
        applyInsetsForActivity(findViewById(R.id.system_left_spacer), findViewById(R.id.system_right_spacer), findViewById(R.id.system_top_spacer));
        applyInset(findViewById(R.id.system_bottom_spacer), InsetLocation.BOTTOM, InsetMode.SET);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_settings) { //starts the settings activity
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startForResult.launch(intent);
        } else if (id == R.id.item_about) { //starts the about activity
            Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intentAbout);
        } else if (id == R.id.item_delete_all) { //shows the delete dialog
            records.showDeleteAllDialog();
        }

        return true;
    }

    /*
     * If the search bar contains text, show the delete-all-text button
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        clearButton.setVisibility(s.toString().isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        //nothing
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.imageButtonClear) {
            searchText.setText("");
        }

        if (v.getId() == R.id.linearLayoutSelectSearchEngines) {
            showSearchEngineSelectPopup(v);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        records.load();
        focusSearchBar();
        setSearchEngineIcon(searchEngines.selectedItem());
    }

    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getBooleanExtra("RELOAD_EDGE_TO_EDGE", false)) {
                recreate();
            }
        }
    }

    /**
     * Starts the search with the text in searchText
     *
     */
    public void startSearch() {
        searchEngines.updateCustomSearchUris();
        SearchEngineItem item = searchEngines.selectedItem();
        String baseUrl = item.uri();                                                                //get the base url of the search engine
        String text = searchText.getText().toString().trim();                                       //get search text with rmoved whitespace

        //workaround for the wrong google url
        if (baseUrl.equals("https://www.google.de/#q=%s")){
            baseUrl = "https://www.google.de/search?q=%s";
        } else if (baseUrl.equals("https://www.google.com/#q=%s")){
            baseUrl = "https://www.google.com/search?q=%s";
        }

        Uri searchUrl = null;

        if (item.isUriNotValid()) {                                                                 //custom search url must contain the %s part
            showToast(getString(R.string.string_doesnt_contain_placeholder), this);
            return;
        }

        try {
            searchUrl = Uri.parse(baseUrl.replace("%s",URLEncoder.encode(text, "UTF-8")));          //try to encode the string to a url. eg "this is a test" gets converted to "this+is+a+test"
        } catch (UnsupportedEncodingException e) {
            showToast(getString(R.string.unsupported_search_character), this);
        }

        if (searchUrl != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, searchUrl);

            try {
                startActivity(browserIntent);                                                       //try to start the browser, if there is one installed
            } catch (ActivityNotFoundException e) {
                showToast(getString(R.string.unsupported_search_string), this);
            }
        }

        searchText.setSelection(0, searchText.length());                                            //select all text to allow for easy delete or modification on resume
        records.add(text);                                                                          //move search term to front of history

        if (getSavedCloseAfterSearch()) {
            finish();
        }
    }

    /**
     * Applies the given text to the search bar and sets its cursor to the last character
     *
     * @param newText the new text to show
     */
    public void setSearchText(String newText) {
        searchText.setText(newText);
        searchText.setSelection(searchText.length());
    }

    /**
     * Appends the given text to the search bar and sets its cursor to the last character
     *
     * @param newText the new text to show
     */
    public void appendSearchText(String newText) {
        searchText.append(newText);
        searchText.setSelection(searchText.length());
    }


    /**
     *  Focuses the search bar and shows the keyboard
     */
    public void focusSearchBar(){
        searchText.requestFocus();
        searchText.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    /*
     * Gets the text from the search bar and starts the search
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //just start the search, because the ime id is not always the same on every device...
        startSearch();

        return true;
    }

    private void showSearchEngineSelectPopup(View anchorView) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.search_engine_select_listview, null);
        ListView listView = popupView.findViewById(R.id.popup_list);

        CustomMenuAdapter adapter = new CustomMenuAdapter(this, searchEngines.items());
        listView.setAdapter(adapter);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            SearchEngineItem item = searchEngines.items().get(position);

            if (item.isCustomEngine() && item.isUriNotValid()) {
                showToast(getString(R.string.setup_custom_search_in_settings), this);
            } else {
                putSavedString(PREF_SEARCH_KEY, item.key());
                setSearchEngineIcon(item);
                popupWindow.dismiss();
            }
        });

        popupWindow.setElevation(4);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dialog_popup_background));
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    private void setSearchEngineIcon(SearchEngineItem item) {
        int searchbarIconRes = item.searchbarIconRes();
        searchEngineSelectButton.setImageResource(searchbarIconRes != -1 ? searchbarIconRes : item.iconRes());

        searchEngineSelectButton.setAdjustViewBounds(true);
        searchEngineSelectButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
