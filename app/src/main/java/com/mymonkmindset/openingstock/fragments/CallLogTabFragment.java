package com.mymonkmindset.openingstock.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.adapters.CallLogsAdapter;
import com.mymonkmindset.openingstock.dto.ApiResponse;
import com.mymonkmindset.openingstock.dto.PeopleDto;
import com.mymonkmindset.openingstock.models.CallLogItem;
import com.mymonkmindset.openingstock.models.Person;
import com.mymonkmindset.openingstock.models.PersonWithPhone;
import com.mymonkmindset.openingstock.utils.DataLoader;
import com.mymonkmindset.openingstock.utils.NetworkClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CallLogTabFragment extends Fragment {

    private RecyclerView rvCallLogs;
    private SharedPreferences prefs;
    private List<CallLogItem> callLogs;

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

        // 1. Get SharedPreferences using requireContext()
        prefs = requireContext().getSharedPreferences("OPENING_STOCK_PREF", Context.MODE_PRIVATE);
        syncCallLogs();
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
            this.callLogs = new ArrayList<>();
            DataLoader.loadRealCallLogs(requireContext(), this.callLogs);
            rvCallLogs.setLayoutManager(new LinearLayoutManager(requireContext()));
            CallLogsAdapter adapter = new CallLogsAdapter(this.callLogs);
            rvCallLogs.setAdapter(adapter);
        }
    }

    private void syncCallLogs(){
        View view = getView();
        Gson gson = new Gson();
        String backendUrl = prefs.getString("backend_url", "");
        String uri = backendUrl + "v4/people/sync-call-logs";

        if (backendUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Backend URL is not set", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(requireContext(), uri, Toast.LENGTH_SHORT).show();
        NetworkClient.post(uri, gson.toJson(callLogs), new NetworkClient.HttpResponseCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(String responseBody) {
                if (!isAdded()) return; // Protect against detached fragment
//                        if (printData != null) printData.setText(responseBody);
//                Gson g = new Gson();
//                ApiResponse<List<PeopleDto>> apiResponse = g.fromJson(responseBody, new TypeToken<ApiResponse<List<PeopleDto>>>() {}.getType());
////
//                List<PersonWithPhone> pl = (apiResponse != null && apiResponse.getData() != null)
//                        ? apiResponse.getData().stream()
//                          .map(d -> PersonWithPhone.builder()
//                                    .person(Person.builder()
//                                            .name(d.getName())
//                                            .build())
//                                    .phoneNumber(d.getPhone()) // map corresponding fields
//                                    .build())
//                          .collect(Collectors.toList()) // or .collect(Collectors.toList()) if Java < 16 / Android minSdk < 34
//                        : Collections.emptyList();
////
//                callLogs.addAll(pl);
//                callLogs.setText("Count: " + String.valueOf(callLogs.size()));
//                callLogs.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Response: " + responseBody, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                if (!isAdded()) return;
//                if (chipCount != null) chipCount.setText( uri +" " + errorMessage);
                Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
