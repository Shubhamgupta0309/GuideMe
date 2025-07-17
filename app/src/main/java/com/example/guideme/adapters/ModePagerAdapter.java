package com.example.guideme.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.guideme.fragments.AssistantFragment;
import com.example.guideme.fragments.NavigationFragment;
import com.example.guideme.fragments.ReadingFragment;

public class ModePagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    public ModePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new NavigationFragment();
            case 1: return new AssistantFragment();
            case 2: return new ReadingFragment();
            default: return new NavigationFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}