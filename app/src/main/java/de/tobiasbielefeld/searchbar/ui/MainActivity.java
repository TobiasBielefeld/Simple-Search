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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.helper.Records;
import de.tobiasbielefeld.searchbar.ui.settings.Settings;

import static de.tobiasbielefeld.searchbar.SharedData.*;

public class MainActivity extends CustomAppCompatActivity implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener {

    public EditText searchText;
    private ImageView clearButton;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchText = (EditText) findViewById(R.id.editTextSearch);
        clearButton = (ImageView) findViewById(R.id.imageButtonClear);

        searchText.addTextChangedListener(this);
        searchText.setOnEditorActionListener(this);

        setSupportActionBar(toolbar);

        records = new Records(this);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings: //starts the settings activity
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
                break;
            case R.id.item_delete_all: //shows the delete dialog
                records.showDeleteAllDialog();
                break;
        }

        return true;
    }

    /*
     * If the search bar contains text, show the delete-all-text button
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        clearButton.setVisibility(s.toString().equals("") ? View.GONE : View.VISIBLE);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        records.load();
    }

    /**
     * Shows the given text as a toast. New texts override the old one.
     *
     * @param text The text to show
     */
    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else
            toast.setText(text);

        toast.show();
    }

    /**
     * Starts the search with the given text
     *
     * @param text The text to search
     */
    public void startSearch(String text) {
        String baseUrl = getSavedString(PREF_SEARCH_URL, DEFAULT_SEARCH_URL);                 //get the base url of the search engine

        Uri searchUrl = null;

        if (!baseUrl.contains("%s")) {                                                              //custom search url must contain the %s part
            showToast(getString(R.string.string_doesnt_contain_placeholder));
            return;
        }

        try {
            searchUrl = Uri.parse(baseUrl.replace("%s",URLEncoder.encode(text, "UTF-8")));          //try to encode the string to a url. eg "this is a test" gets converted to "this+is+a+test"
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showToast(getString(R.string.unsupported_search_character));
        }

        if (searchUrl != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, searchUrl);

            try {
                startActivity(browserIntent);                                                       //try to start the browser, if there is one installed
                setSearchText("");
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                showToast(getString(R.string.unsupported_search_string));
            }
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

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);
    }

    /*
     * Gets the text from the search bar and starts the search
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        //just start the search, because the ime id is not always the same on every device...
        String text = v.getText().toString();
        startSearch(text);
        records.add(text);

        return true;
    }
}
