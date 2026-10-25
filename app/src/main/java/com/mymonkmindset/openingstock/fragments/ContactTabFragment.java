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
import com.mymonkmindset.openingstock.adapters.ContactsAdapter;
import com.mymonkmindset.openingstock.models.ContactItem;
import com.mymonkmindset.openingstock.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class ContactTabFragment extends Fragment {

    private RecyclerView rvContacts;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    loadContacts();
                } else {
                    Toast.makeText(getContext(), "Contacts permission denied. Showing sample contacts.", Toast.LENGTH_SHORT).show();
                    loadContacts();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvContacts = view.findViewById(R.id.rvContacts);
        checkAndLoadContacts();
    }

    private void checkAndLoadContacts() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void loadContacts() {
        if (rvContacts != null && isAdded()) {
            List<ContactItem> contacts = new ArrayList<>();
            DataLoader.loadRealContacts(requireContext(), contacts);
            rvContacts.setLayoutManager(new LinearLayoutManager(requireContext()));
            ContactsAdapter adapter = new ContactsAdapter(contacts);
            rvContacts.setAdapter(adapter);
        }
    }
}
