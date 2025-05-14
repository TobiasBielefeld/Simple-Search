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

import static de.tobiasbielefeld.searchbar.SharedData.showToast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.tobiasbielefeld.searchbar.R;

public class FaviconLoader {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void loadFavicon(Context context, String pageUrl, ImageView imageView, ProgressBar progressBar) {
        executor.execute(() -> {
            imageView.post(() -> imageView.setVisibility(ImageView.GONE));
            progressBar.post(() -> progressBar.setVisibility(ImageView.VISIBLE));

            Bitmap bitmap = getBitmapFromIconLink(pageUrl);

            if (bitmap == null) {
                bitmap = getBitMapFromFavicon(pageUrl);
            }

            Bitmap finalBitmap = bitmap;
            mainHandler.post(() -> {
                imageView.post(() -> imageView.setVisibility(ImageView.VISIBLE));
                progressBar.post(() -> progressBar.setVisibility(ImageView.GONE));

                if (finalBitmap != null) {
                    imageView.setImageBitmap(finalBitmap);
                } else {
                    showToast(context.getString(R.string.dialog_edit_search_engine_favicon_load_error), context);
                }
            });
        });
    }

    private static Bitmap getBitmapFromIconLink(String pageUrl) {
        try {
            String url = pageUrl.replace("%s", "test");
            Document doc = Jsoup.connect(url).get();
            Element iconLink = doc.select("link[rel~=(?i)^(shortcut icon|icon)]").first();

            if (iconLink != null) {
                return getBitmap(iconLink.attr("abs:href"));
            }
        } catch (Exception ignored) { }

        return null;
    }

    private static Bitmap getBitMapFromFavicon(String pageUrl) {
        try {
            URL url = new URL(pageUrl);
            String processedUrl = url.getProtocol() + "://" + url.getHost();

            if (url.getPort() != -1) {
                processedUrl += ":" + url.getPort();
            }

            return getBitmap(processedUrl + "/favicon.ico");
        } catch (Exception ignored) { }

        return null;
    }

    private static Bitmap getBitmap(String iconHref) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(iconHref).openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }
}