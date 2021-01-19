package com.pheonix.org.hallaka.Activity.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pheonix.org.hallaka.Adapters.TabsAccessAdapterHome;
import com.pheonix.org.hallaka.R;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ViewPager myViewPager;
    TabLayout myTabLayout;
    TabsAccessAdapterHome myTabsAccessAdapterHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myTabLayout = findViewById(R.id.mainTabs);
        myViewPager = findViewById(R.id.mainTab_pages);

        myTabsAccessAdapterHome = new TabsAccessAdapterHome(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessAdapterHome);

        myTabLayout.setupWithViewPager(myViewPager);

        Objects.requireNonNull(myTabLayout.getTabAt(0)).setIcon(R.drawable.ic_baseline_home_24);
        Objects.requireNonNull(myTabLayout.getTabAt(1)).setIcon(R.drawable.ic_cart);

    }

    public void hideBlack(View view) {
        view.setVisibility(View.GONE);
        findViewById(R.id.productLayId).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.productCount)).setText("1");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Alert").setMessage("Do you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }
}