package com.example.btcrelease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.btcrelease.Adapters.TabAdapter;
import com.example.btcrelease.Interfaces.FirebaseBoolCallback;
import com.example.btcrelease.utils.apiUtils;
import com.example.btcrelease.utils.baseUtils;
import com.example.btcrelease.utils.firebaseUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem trendingItem, economyItem, environmentItem, socialItem;
    TabAdapter tabAdapter;
    TextView fancyTitle;
    ImageButton settingsButton, addFriendsButton;
    ViewPager2 newsPage, addUserPage;
    String currentTab;
    String apiKey = apiUtils.getApiKey();
    Toolbar toolbar;
    String[] cats = new String[]{"TrendingNews", "EconomyNews", "EnvironmentNews", "SocietyNews"};
    MaterialButton btcHeader;
    private final boolean DEBUG_NEWS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setSupportActionBar(findViewById(R.id.toolbar));

        // create instances of buttons
        toolbar = findViewById(R.id.toolbar);
        fancyTitle = findViewById(R.id.NeatUserTitle);
        settingsButton = findViewById(R.id.UserProfile);
        addFriendsButton = findViewById(R.id.AddFriends);
        btcHeader = findViewById(R.id.QratedHeader);

        tabLayout = findViewById(R.id.NewsTabLayout);
        trendingItem = findViewById(R.id.TrendingTab);
        economyItem = findViewById(R.id.EconomyTab);
        environmentItem = findViewById(R.id.EnvironmentTab);
        socialItem = findViewById(R.id.SocietyTab);

        newsPage = findViewById(R.id.fragment_container);





        // Checks if news needs to be updated and if so, does it
        firebaseUtils.checkIfNewsNeedsUpdate(new FirebaseBoolCallback() {
            @Override
            public void onBoolCallback(boolean value) {
                if(value){
                    System.out.println("--------- Updating news ----------");
                    apiUtils.getNewsFromCategory("TrendingNews", apiKey);
                    apiUtils.getNewsFromCategory("EconomyNews", apiKey);
                    apiUtils.getNewsFromCategory("EnvironmentNews", apiKey);
                    apiUtils.getNewsFromCategory("SocietyNews", apiKey);
                    firebaseUtils.resetComments();

                }
                else{
                    //Toast.makeText(MainActivity.this, "Debug: No news update", Toast.LENGTH_SHORT).show();
                }

                tabAdapter = new TabAdapter(getSupportFragmentManager(), 4);
                newsPage.setAdapter(tabAdapter);
                newsPage.setOffscreenPageLimit(3);

                //Here we go!
                fancyTitle.setText(baseUtils.getFancyTitle());

                // Updates when tab is selected
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    System.out.println("FOUND EXTRAS");
                    currentTab = extras.getString("tab");
                }
                if(!Objects.equals(currentTab, "")){
                    for(int s = 0; s < cats.length; s++){
                        if(cats[s].equals(currentTab)){
                            newsPage.setCurrentItem(s);
                            tabLayout.selectTab(tabLayout.getTabAt(s));

                        }
                    }
                }

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        newsPage.setCurrentItem(tab.getPosition());
                        tabAdapter.createFragment(tab.getPosition());
                        tabAdapter.notifyDataSetChanged();
                        currentTab = cats[tab.getPosition()];

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                // Update Viewpager if user swiped
                newsPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {tabLayout.selectTab(tabLayout.getTabAt(position));}});


                // Listeners for settings buttons / add friends buttons
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // go to settings activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, Settings.class);
                                intent.putExtra("tab", currentTab);
                                startActivity(intent);
                                finish();
                            }

                        },0);
                    }
                });

                addFriendsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // go to add users activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, AddUsersActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        },0);

                    }
                });
                btcHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://anaypant.github.io/bethechange/"));
                        startActivity(intent);
                    }
                });
            }
        }, DEBUG_NEWS);






    }
}