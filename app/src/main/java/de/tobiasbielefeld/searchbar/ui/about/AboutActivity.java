package de.tobiasbielefeld.searchbar.ui.about;

import static de.tobiasbielefeld.searchbar.helper.InsetHelper.*;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import de.tobiasbielefeld.searchbar.R;
import de.tobiasbielefeld.searchbar.classes.CustomAppCompatActivity;
import de.tobiasbielefeld.searchbar.classes.TabsPagerAdapter;
import de.tobiasbielefeld.searchbar.databinding.ActivtyAboutBinding;

public class AboutActivity extends CustomAppCompatActivity {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.about_tab_1, R.string.about_tab_2, R.string.about_tab_3};
    private static final Fragment[] FRAGMENTS = new Fragment[]{new InformationFragment(), new LicenseFragment(), new ChangeLogFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivtyAboutBinding binding = ActivtyAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        systemTopSpacer = binding.systemTopSpacer;

        TabsPagerAdapter sectionsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager(), TAB_TITLES, FRAGMENTS);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        ActionBar actionbar = getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> finish());
        applyInsetsForActivity(binding.systemLeftSpacer, binding.systemRightSpacer, systemTopSpacer);
    }
}