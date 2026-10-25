package com.mymonkmindset.openingstock.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.card.MaterialCardView;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.activities.FileManagerActivity;
import com.mymonkmindset.openingstock.activities.ProfileActivity;

public class DashboardTabFragment extends Fragment {

    private MaterialCardView blockReminders, blockPersons, blockContacts, blockCallLogs, blockFileManager, blockProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blockReminders = view.findViewById(R.id.blockReminders);
        blockPersons = view.findViewById(R.id.blockPersons);
        blockContacts = view.findViewById(R.id.blockContacts);
        blockCallLogs = view.findViewById(R.id.blockCallLogs);
        blockFileManager = view.findViewById(R.id.blockFileManager);
        blockProfile = view.findViewById(R.id.blockProfile);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);

        if (blockReminders != null) {
            blockReminders.setOnClickListener(v -> {
                if (viewPager != null) viewPager.setCurrentItem(1, true);
            });
        }

        if (blockPersons != null) {
            blockPersons.setOnClickListener(v -> {
                if (viewPager != null) viewPager.setCurrentItem(2, true);
            });
        }

        if (blockContacts != null) {
            blockContacts.setOnClickListener(v -> {
                if (viewPager != null) viewPager.setCurrentItem(3, true);
            });
        }

        if (blockCallLogs != null) {
            blockCallLogs.setOnClickListener(v -> {
                if (viewPager != null) viewPager.setCurrentItem(4, true);
            });
        }

        if (blockFileManager != null) {
            blockFileManager.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), FileManagerActivity.class));
            });
        }

        if (blockProfile != null) {
            blockProfile.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), ProfileActivity.class));
            });
        }
    }
}
