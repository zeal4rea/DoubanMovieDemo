package com.zeal4rea.doubanmoviedemo.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private boolean isDrawerOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Utils.setCustomDensity(this);
        setUpToolbarAndDrawer();

        View drawer = findViewById(R.id.main$drawer);
        drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return isDrawerOpen;
            }
        });
        SwitchCompat mSwitch = findViewById(R.id.drawer$switch_night_mode);
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
            fragmentTransaction.add(R.id.main_content$container, mainTabsFragment, "main");
        }
        fragmentTransaction.show(mainTabsFragment);
        fragmentTransaction.commit();
    }

    private void setUpToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.main_content$toolbar);
        mSearchView = findViewById(R.id.main_content$search_view);
        mDrawerLayout = findViewById(R.id.main$drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_desc$open, R.string.drawer_desc$close);
        mDrawerLayout.addDrawerListener(toggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpen = false;
            }
        });
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
                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                Bundle b = new Bundle();
                b.putString("q", query);
                intent.putExtra("b", b);
                MainActivity.this.startActivity(intent);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        /*Window window = getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int options = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(options);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
    }
}
