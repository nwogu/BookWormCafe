package com.example.android.bwc;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hub_pager);

        ViewPager listView = (ViewPager) findViewById(R.id.hubpager);
        HubFragmentAdapter flexFragmentAdapter = new HubFragmentAdapter(getSupportFragmentManager());

        listView.setAdapter(flexFragmentAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(listView);
    }
}
