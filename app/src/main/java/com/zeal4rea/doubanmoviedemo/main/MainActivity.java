package com.zeal4rea.doubanmoviedemo.main;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.query.QueryActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.SimpleSearchView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private SimpleSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setCustomDensity(this);
        setUpToolbarAndDrawer();

        NavigationView navigationView = findViewById(R.id.main$nav_view);
        SwitchCompat mSwitch = navigationView.getHeaderView(0).findViewById(R.id.drawer$switch_night_mode);
        boolean nightMode = Utils.sharePreferenceGetBoolean(BaseContants.NIGHT_MODE, false);
        mSwitch.setChecked(nightMode);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Toast.makeText(MainActivity.this, "夜间模式:" + isChecked, Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                Utils.sharePreferencePutBoolean(BaseContants.NIGHT_MODE, isChecked);
                recreate();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainTabsFragment mainTabsFragment = (MainTabsFragment) fragmentManager.findFragmentByTag("main");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mainTabsFragment == null) {
            mainTabsFragment = new MainTabsFragment();
            fragmentTransaction.add(R.id.main$container, mainTabsFragment, "main");
        }
        fragmentTransaction.show(mainTabsFragment);
        fragmentTransaction.commit();
    }

    private void setUpToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.main$toolbar);
        mSearchView = findViewById(R.id.main$search_view);
        mDrawerLayout = findViewById(R.id.main$drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_desc$open, R.string.drawer_desc$close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(search);
        mSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryActivity.newIntent(MainActivity.this, query, null);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (mSearchView.isSearchOpen()) {
            mSearchView.hideSearch();
            return;
        }
        super.onBackPressed();
    }
}
