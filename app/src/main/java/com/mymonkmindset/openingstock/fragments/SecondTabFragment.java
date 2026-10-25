package com.mymonkmindset.openingstock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.adapters.CustomAdapter;
import com.mymonkmindset.openingstock.interfaces.Listable;
import com.mymonkmindset.openingstock.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class SecondTabFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeRecycleView(view);
    }

    private void makeRecycleView(@NonNull View view) {
        RecyclerView rvCardsList = view.findViewById(R.id.rvCardsList);
        if (rvCardsList != null) {
            List<Listable> persons = new ArrayList<>();
            DataLoader.loadPersonData(persons);
            rvCardsList.setLayoutManager(new LinearLayoutManager(requireContext()));
            CustomAdapter customAdapter = new CustomAdapter(persons);
            rvCardsList.setAdapter(customAdapter);
        }
    }
}
