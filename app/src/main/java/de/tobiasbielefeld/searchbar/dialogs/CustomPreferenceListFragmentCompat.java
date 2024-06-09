package de.tobiasbielefeld.searchbar.dialogs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.ui.settings.helpers.CustomListPreference;

public abstract class CustomPreferenceListFragmentCompat extends CustomPreferenceDialogFragmentCompat {

    private RadioGroup radioGroup;
    private boolean loadedBundleState = false;
    protected int selectedIndex = -1;

    protected abstract void selectionChanged();

    protected abstract int getSelectedIndex();
    protected void onBindView(View view) {
        if (getPreference() instanceof CustomListPreference) {
            int id = ((CustomListPreference) getPreference()).getTitleArrayId();
            String[] values = getResources().getStringArray(id);

            radioGroup = view.findViewById(R.id.radio_group);
            int height = (int) getResources().getDimension(R.dimen.dialog_radio_button_height);

            if (!loadedBundleState) {
                selectedIndex = getSelectedIndex();
            }

            for (int i = 0; i < values.length; i++) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(values[i]);
                radioButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioButton.setHeight(height);
                radioButton.setOnClickListener(this::onClick);
                radioGroup.addView(radioButton);

                if (i == selectedIndex) {
                    radioGroup.check(radioButton.getId());
                }
            }
        }
    }

    private void onClick(View view) {
        selectedIndex = radioGroup.indexOfChild(view);
        selectionChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt("selectedIndex");
            loadedBundleState = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedIndex", selectedIndex);
    }
}
