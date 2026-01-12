package com.example.frontend.ui.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.*;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.frontend.ui.admin.soutenance.SoutenancesFragment;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new StudentFragment();
            case 1:
                return new EncadrantFragment();
            case 2:
                return new SoutenancesFragment(); // ✅ NOUVEAU, indépendant
            default:
                return new StudentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // ✅ avant: 2
    }
}
