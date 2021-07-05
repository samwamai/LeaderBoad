        package com.gads.leaderboad;

        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.viewpager.widget.ViewPager;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.View;

        import com.google.android.material.appbar.MaterialToolbar;
        import com.google.android.material.tabs.TabLayout;


        public class MainActivity extends AppCompatActivity {

            private MaterialToolbar toolbar;
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);


                 TabLayout tabLayout = findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText("Learning Leaders")); // Set the text for each tab.
                tabLayout.addTab(tabLayout.newTab().setText("Skill IQ Leaders"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);    // Set the tabs to fill the entire layout.

                final ViewPager viewPager = findViewById(R.id.pager);        // Use PagerAdapter to manage page views in fragments.Each page is represented by its own fragment.
                final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);

                // Setting a listener for clicks.
              viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
              tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                  @Override
                  public void onTabSelected(TabLayout.Tab tab) {
                      viewPager.setCurrentItem(tab.getPosition());
                  }

                  @Override
                  public void onTabUnselected(TabLayout.Tab tab) {

                  }

                  @Override
                  public void onTabReselected(TabLayout.Tab tab) {
                  }
              });

            }

            public void submit_button_clicked(View view) {
                Intent intent=(new Intent(MainActivity.this,SubmissionActivity.class));
                startActivity(intent);
            }

        }