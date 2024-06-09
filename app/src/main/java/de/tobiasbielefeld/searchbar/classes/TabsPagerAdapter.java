/*
 * Copyright (C) 2016  Tobias Bielefeld
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

package de.tobiasbielefeld.searchbar.classes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Adapter for the tabs
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final List<String> titleList = new ArrayList<>();
    private final List<Fragment> fragmentList = new ArrayList<>();

    public TabsPagerAdapter(Context context, FragmentManager fm, int[] titleIds, Fragment[] fragments) {
        super(fm);

        for (int id: titleIds) {
            titleList.add(context.getString(id));
        }

        fragmentList.addAll(Arrays.asList(fragments));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int index) {
        return fragmentList.get(index);
    }

}