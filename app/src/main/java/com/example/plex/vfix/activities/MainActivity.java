package com.example.plex.vfix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.plex.vfix.R;
import com.example.plex.vfix.adapters.MainFragmentViewPageAdaptor;
import com.example.plex.vfix.fragments.CurrentFragment;
import com.example.plex.vfix.fragments.HistoryFragment;
import com.example.plex.vfix.utils.Configuration;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final int SUBMIT_NEW_FIX = 0x1;


    ArrayList<Fragment> list = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,NewFixActivity.class),SUBMIT_NEW_FIX);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///set profile

        View header_view = navigationView.getHeaderView(0);

        TextView name = (TextView) header_view.findViewById(R.id.main_user_name);
        TextView tel  = (TextView) header_view.findViewById(R.id.main_tel);

        name.setText(Configuration.myself.getNickname() == null ? "微修用户":Configuration.myself.getNickname());
        tel.setText(Configuration.myself.getMobile() == null ? "138****0000":Configuration.myself.getMobile());


        ///////////////////////////////////////////////////////////////////////////////////


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.main_view_page);


        titles.add("当前维修");
        titles.add("历史维修");
        list.add(CurrentFragment.newInstance());
        list.add(HistoryFragment.newInstance());
        viewPager.setAdapter(new MainFragmentViewPageAdaptor(titles,list,getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);


//        Button btn = (Button) findViewById(R.id.button);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
//       getMenuInflater().inflate();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            ProfileActivity.startActivity(this,"A","1857as46");
        } else if (id == R.id.nav_fix_now) {
            NewFixActivity.startActivity(this);
        } else if (id == R.id.nav_slideshow) {
            WebViewActivity.startActivityInternal(this,"http://ujn.edu.cn/page/telephone");
        //} else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_about) {
            WebViewActivity.startActivityInternal(this,"http://ibytes.cn/aboutme");
        } else if (id == R.id.nav_provision) {
            WebViewActivity.startActivityInternal(this,"http://ibytes.cn/copyright");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
