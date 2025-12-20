package com.example.frontend.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.frontend.fragment.EtudiantsDisponiblesFragment;
import com.example.frontend.fragment.MesEtudiantsFragment;

public class EncadrantPagerAdapter extends FragmentStateAdapter {

    public EncadrantPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new EtudiantsDisponiblesFragment();
        } else {
            return new MesEtudiantsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
