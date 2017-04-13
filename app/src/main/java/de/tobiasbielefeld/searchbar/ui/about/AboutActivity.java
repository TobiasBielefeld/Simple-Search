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

package de.tobiasbielefeld.searchbar.ui.about;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;

import static de.tobiasbielefeld.searchbar.SharedData.*;

/**
 * This is created with help of this article: http://simpledeveloper.com/how-to-create-android-swipe-views-tabs/
 * The About activity contains 3 tabs. The content of the tabs is in the fragments
 *
 * This stuff is depreciated, but it works and looks very fine :)
 */
@SuppressWarnings("deprecation")
public class AboutActivity extends CustomAppCompatActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_about);

        actionBar = getSupportActionBar();
        viewPager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tabFirst = actionBar.newTab().setText(getString(R.string.about_tab_1)).setTabListener(this);
        ActionBar.Tab tabSecond = actionBar.newTab().setText(getString(R.string.about_tab_2)).setTabListener(this);
        ActionBar.Tab tabThird = actionBar.newTab().setText(getString(R.string.about_tab_3)).setTabListener(this);

        actionBar.addTab(tabFirst);
        actionBar.addTab(tabSecond);
        actionBar.addTab(tabThird);
        actionBar.selectTab(tabFirst);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //only menu item is the back button in the action bar so finish this activity
        finish();
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
}
