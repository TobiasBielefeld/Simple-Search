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

package de.tobiasbielefeld.searchbar.dialogs;

import static de.tobiasbielefeld.searchbar.SharedData.byteArrayToBitmap;
import static de.tobiasbielefeld.searchbar.SharedData.database;
import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.helper.AfterViewTreeObserved;
import de.tobiasbielefeld.searchbar.helper.LoadingTaskHelper;
import de.tobiasbielefeld.searchbar.models.SearchEngine;


public class DialogManageSearchEngines extends CustomPreferenceDialogFragmentCompat {

    private static final String KEY_SELECTED_INDEX = "key_selected_index";
    private record ListItem(LinearLayout layoutMain, LinearLayout layoutButtons) {}
    private boolean isLoading = false;
    private int selectedIndex = -1;
    private LinearLayout layoutContainer;
    private RelativeLayout layoutLoading;
    private ScrollView scrollView;
    private Bundle savedState;

    private final List<ListItem> listItems = new ArrayList<>();
    private List<SearchEngine> engines;
    private final TypedValue typedValue = new TypedValue();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        savedState = savedInstanceState;
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    protected void onBindView(View view) {
        requireContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        layoutContainer = view.findViewById(R.id.linear_layout_items);
        scrollView = view.findViewById(R.id.scrollview_container);
        layoutLoading = view.findViewById(R.id.layout_loading);

        startLoading();

        AfterViewTreeObserved.run(layoutLoading, () ->
                new LoadingTaskHelper(layoutLoading,
                        this::loadListItems,
                        () -> {
                            showListItems();
                            stopLoading();

                            if (savedState != null) {
                                int index = savedState.getInt(KEY_SELECTED_INDEX);

                                if (index >= 0 && index < listItems.size()) {
                                    showButtonsFromListItem(index);
                                }
                            }
                        }
                ).execute()
        );
    }

    @Override
    protected void onClickOkay(AlertDialog dialog) {
        if (engines != null) {
            DialogEditSearchEngine editDialog = new DialogEditSearchEngine(getActivity(), null, engines);
            editDialog.show();
            dialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex);
    }

    private void loadListItems() {
        listItems.clear();
        try {
            engines = database.getSearchEngines();
            long numSearchEngines = engines.size();
            Context context = requireContext();

            for (SearchEngine engine: engines) {
                LinearLayout item = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_manage_search_engines_item, null);
                ImageView imageView = item.findViewById(R.id.image_view);
                TextView textViewName = item.findViewById(R.id.dialog_manage_search_engines_name);
                TextView textViewUri = item.findViewById(R.id.dialog_manage_search_engines_uri);
                LinearLayout layoutButtons = item.findViewById(R.id.dialog_manage_search_engines_layout_buttons);
                Button buttonEdit = item.findViewById(R.id.dialog_manage_search_engines_button_edit);
                Button buttonDelete = item.findViewById(R.id.dialog_manage_search_engines_button_delete);
                imageView.setImageBitmap(byteArrayToBitmap(engine.icon));
                textViewName.setText(engine.label);
                textViewUri.setText(engine.uri);
                item.setOnClickListener(this::onItemClick);
                listItems.add(new ListItem(item, layoutButtons));

                buttonEdit.setOnClickListener((View button) -> {
                    DialogEditSearchEngine dialog = new DialogEditSearchEngine(getActivity(), engine, engines);
                    dialog.show();
                    dismiss();
                });

                if (numSearchEngines == 1) {
                    buttonDelete.setVisibility(View.GONE); // cannot delete last entry
                } else {
                    int currentIndex = listItems.size() - 1;
                    buttonDelete.setOnClickListener((View button) -> hideButtonsFromListItem(currentIndex, () -> deleteEntry(engine)));
                }
            }
        } catch (IllegalStateException e) {
            // context went missing due to rotating screen or similar, just ignore that
        }
    }

    private void onItemClick(View view) {
        int prevIndex = selectedIndex;
        int newIndex = layoutContainer.indexOfChild(view);

        if (newIndex == prevIndex || isLoading) {
            return;
        }

        showButtonsFromListItem(newIndex);
        hideButtonsFromListItem(prevIndex, null);
    }

    private void showListItems() {
        layoutContainer.removeAllViews();
        selectedIndex = -1;

        for (ListItem listItem: listItems) {
            layoutContainer.addView(listItem.layoutMain);
        }
    }

    private void deleteEntry(SearchEngine entry) {
        startLoading();
        new LoadingTaskHelper(layoutLoading,
                () -> {
                    database.deleteSearchEngine(entry);
                    loadListItems();
                },
                () -> {
                    showToast(getString(R.string.settings_manage_search_engines_deleted), getContext());
                    showListItems();
                    stopLoading();
                }
        ).execute();
    }

    private void startLoading() {
        setCancelable(false);
        isLoading = true;
    }

    private void stopLoading() {
        setCancelable(true);
        isLoading = false;
    }

    private void showButtonsFromListItem(int index) {
        selectedIndex = index;
        listItems.get(index).layoutMain.setBackgroundResource(R.drawable.dialog_highlight);

        viewExpand(listItems.get(index).layoutButtons, () -> {
            final int itemBottom = listItems.get(index).layoutMain.getBottom();
            final int scrollHeight = scrollView.getHeight();

            if (itemBottom > (scrollView.getScrollY() + scrollHeight)) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, itemBottom - scrollHeight));
            }
        });
    }

    private void hideButtonsFromListItem(int index, Runnable runOnEnd) {
        if (index != -1) {
            listItems.get(index).layoutMain.setBackgroundResource(typedValue.resourceId);
            viewCollapse(listItems.get(index).layoutButtons, runOnEnd);
        } else {
            if (runOnEnd != null) {
                runOnEnd.run();
            }
        }
    }

    private static void viewExpand(final View v, @Nullable Runnable onEndListener) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        // it also makes animation more smooth
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }


        };

        if (onEndListener != null) {
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    onEndListener.run();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }

        // 1dp/ms
        a.setDuration(200);
        v.startAnimation(a);
    }

    private static void viewCollapse(final View v, Runnable runOnEnd) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(200);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (runOnEnd != null) {
                    runOnEnd.run();
                }
            }
        });
        v.startAnimation(a);
    }
}
