package com.example.frontend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.frontend.ui.admin.AdminPagerAdapter;
import com.example.frontend.ui.admin.soutenance.SoutenancesFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Adapter avec 3 fragments
        viewPager.setAdapter(new AdminPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, pos) -> {
                    switch (pos) {
                        case 0:
                            tab.setText("Étudiants");
                            break;
                        case 1:
                            tab.setText("Encadrants");
                            break;
                        case 2:
                            tab.setText("Soutenances");
                            break;
                    }
                }
        ).attach();
    }
}
