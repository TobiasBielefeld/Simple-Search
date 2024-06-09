

package de.tobiasbielefeld.searchbar.ui.settings;

import static de.tobiasbielefeld.searchbar.SharedData.findBestLocaleMatchIndex;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;
import de.tobiasbielefeld.searchbar.R;


public class PreferenceLanguage extends CustomListPreference {

    Context context;

    public PreferenceLanguage(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public PreferenceLanguage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PreferenceLanguage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected int getCurrentValue() {
        String[] availableLanguages = context.getResources().getStringArray(R.array.pref_language_values);
        String systemLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        return findBestLocaleMatchIndex(availableLanguages, systemLanguage);
    }

    @Override
    public int getTitleArrayId() {
        return R.array.pref_language_titles;
    }

}
