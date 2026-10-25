package com.mymonkmindset.openingstock.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mymonkmindset.openingstock.fragments.CallLogTabFragment;
import com.mymonkmindset.openingstock.fragments.ContactTabFragment;
import com.mymonkmindset.openingstock.fragments.DashboardTabFragment;
import com.mymonkmindset.openingstock.fragments.FirstTabFragment;
import com.mymonkmindset.openingstock.fragments.ReminderTabFragment;

public class TabsPagerAdapter extends FragmentStateAdapter {

    public TabsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DashboardTabFragment();
            case 1:
                return new ReminderTabFragment();
            case 2:
                return new FirstTabFragment();
            case 3:
                return new ContactTabFragment();
            default:
                return new CallLogTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
