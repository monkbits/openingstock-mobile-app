package com.mymonkmindset.openingstock.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.adapters.CallLogsAdapter;
import com.mymonkmindset.openingstock.models.CallLogItem;
import com.mymonkmindset.openingstock.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class CallLogTabFragment extends Fragment {

    private RecyclerView rvCallLogs;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadCallLogs();
                } else {
                    Toast.makeText(getContext(), "Call log permission denied. Showing sample logs.", Toast.LENGTH_SHORT).show();
                    loadCallLogs();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call_log_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCallLogs = view.findViewById(R.id.rvCallLogs);
        checkAndLoadCallLogs();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAndLoadCallLogs();
    }

    private void checkAndLoadCallLogs() {
        if (getContext() == null) return;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED) {
            loadCallLogs();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG);
        }
    }

    private void loadCallLogs() {
        if (rvCallLogs != null && isAdded()) {
            List<CallLogItem> callLogs = new ArrayList<>();
            DataLoader.loadRealCallLogs(requireContext(), callLogs);
            rvCallLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
            CallLogsAdapter adapter = new CallLogsAdapter(callLogs);
            rvCallLogs.setAdapter(adapter);
        }
    }
}
