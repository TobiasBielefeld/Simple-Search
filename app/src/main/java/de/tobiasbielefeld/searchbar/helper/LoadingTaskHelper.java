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


package de.tobiasbielefeld.searchbar.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class LoadingTaskHelper {
    private final View loadingLayout;
    private final Runnable backgroundTask;
    private final Runnable uiTask;

    public LoadingTaskHelper(View loadingLayout, Runnable backgroundTask, Runnable uiTask) {
        this.loadingLayout = loadingLayout;
        this.backgroundTask = backgroundTask;
        this.uiTask = uiTask;
    }

    public void execute() {
        showLoadingLayout(loadingLayout);

        Thread thread = new Thread(() -> {
            backgroundTask.run();

            loadingLayout.post(() -> {
                hideLoadingLayout(loadingLayout);

                if (uiTask != null) {
                    uiTask.run();
                }
            });
        });

        thread.start();
    }

    private void showLoadingLayout(View loadingLayout) {
        loadingLayout.setAlpha(0f);
        loadingLayout.setVisibility(View.VISIBLE);
        loadingLayout.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);
    }

    // Method to hide the loading layout with fade-out animation
    private void hideLoadingLayout(View loadingLayout) {
        loadingLayout.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingLayout.setVisibility(View.GONE);
                    }
                });
    }
}