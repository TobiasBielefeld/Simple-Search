package de.tobiasbielefeld.searchbar.classes;

import static de.tobiasbielefeld.searchbar.SharedData.getSavedSearchEngineKey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import de.tobiasbielefeld.searchbar.R;

public class CustomMenuAdapter extends ArrayAdapter<SearchEngineItem> {

    public CustomMenuAdapter(Context context, List<SearchEngineItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView != null ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.search_engine_select_menu_item, parent, false);

        SearchEngineItem item = getItem(position);
        String selectedKey = getSavedSearchEngineKey();

        ImageView icon = view.findViewById(R.id.item_icon);
        TextView text = view.findViewById(R.id.item_text);


        icon.setImageResource(item.iconRes());
        text.setText(item.label());
        view.setBackgroundResource(Objects.equals(item.key(), selectedKey) ? R.drawable.dialog_highlight : android.R.color.transparent);

        return view;
    }
}
