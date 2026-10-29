package com.mymonkmindset.openingstock.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.activities.BaseActivity;
import com.mymonkmindset.openingstock.adapters.PersonAdapter;
import com.mymonkmindset.openingstock.dto.ApiResponse;
import com.mymonkmindset.openingstock.dto.PeopleDto;
import com.mymonkmindset.openingstock.interfaces.HandlesUi;
import com.mymonkmindset.openingstock.models.Person;
import com.mymonkmindset.openingstock.models.PersonWithPhone;
import com.mymonkmindset.openingstock.utils.DataLoader;
import com.mymonkmindset.openingstock.utils.NetworkClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonTabFragment extends Fragment implements HandlesUi {

    private MaterialCardView cardAddPersonForm;
    private EditText etInlineFirstName, etInlineLastName, etInlinePhone, etInlineDescription, etInlineEmail, etInlineLink, etInlineGroup;
    private Button btnInlineSubmit;
    private Chip chipAddBtn, chipShowDeleted, chipShowCloudPerson, chipCount;
    private RecyclerView rvCardsList;

    private List<PersonWithPhone> personList;
    private PersonAdapter personAdapter;
    private Person editingPerson = null;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeRecycleView(view);

        // 1. Get SharedPreferences using requireContext()
        prefs = requireContext().getSharedPreferences("OPENING_STOCK_PREF", Context.MODE_PRIVATE);
        gatherResources();
        setOnClick();
        loadCloudPersons();

    }

    private void makeRecycleView(@NonNull View view) {
        rvCardsList = view.findViewById(R.id.rvCardsList);
        if (rvCardsList != null) {
            personList = new ArrayList<>();
            rvCardsList.setLayoutManager(new LinearLayoutManager(requireContext()));
            personAdapter = new PersonAdapter(personList, new PersonAdapter.OnPersonActionListener() {
                @Override
                public void onEdit(PersonWithPhone item) {
                    startEditPerson(item);
                }

                @Override
                public void onDelete(PersonWithPhone item) {
                    deletePerson(item);
                }

                @Override
                public void onRestore(PersonWithPhone item) {
                    restorePerson(item);
                }
            });
            rvCardsList.setAdapter(personAdapter);
            loadPersons();
        }
    }

    @Override
    public void gatherResources() {
        if (getView() != null) {
            chipAddBtn                  = getView().findViewById(R.id.addPersonBtn);
            cardAddPersonForm       = getView().findViewById(R.id.cardAddPersonForm);
            etInlineFirstName       = getView().findViewById(R.id.etInlineFirstName);
            etInlineLastName        = getView().findViewById(R.id.etInlineLastName);
            etInlinePhone           = getView().findViewById(R.id.etInlinePhone);
            etInlineDescription     = getView().findViewById(R.id.etInlineDescription);
            etInlineEmail           = getView().findViewById(R.id.etInlineEmail);
            etInlineLink            = getView().findViewById(R.id.etInlineLink);
            etInlineGroup           = getView().findViewById(R.id.etInlineGroup);
            btnInlineSubmit         = getView().findViewById(R.id.btnInlineSubmit);
            chipShowDeleted         = getView().findViewById(R.id.chipShowDeleted);
            chipShowCloudPerson     = getView().findViewById(R.id.chipShowCloudPerson);
            chipCount               = getView().findViewById(R.id.chipCount);
        }
    }

    @Override
    public void setOnClick() {
        if (chipAddBtn != null) {
            chipAddBtn.setOnClickListener(v -> toggleAddPersonForm());
        }

        if (btnInlineSubmit != null) {
            btnInlineSubmit.setOnClickListener(v -> saveOrUpdatePerson());
        }

        if (chipShowDeleted != null) {
            chipShowDeleted.setOnCheckedChangeListener((buttonView, isChecked) -> loadPersons());
        }

        if(chipShowCloudPerson != null){
            chipShowCloudPerson.setOnClickListener(view -> {
                personList = new ArrayList<>();
                loadCloudPersons();
            });
        }
    }

    private void loadCloudPersons(){
        View view = getView();
        String backendUrl = prefs.getString("backend_url", "");
        String uri = backendUrl + "v4/people/get-all-people";
        if (backendUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Backend URL is not set", Toast.LENGTH_SHORT).show();
            return;
        }

        NetworkClient.post(uri, "{}", new NetworkClient.HttpResponseCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(String responseBody) {
                if (!isAdded()) return; // Protect against detached fragment
//                        if (printData != null) printData.setText(responseBody);
                Gson g = new Gson();
                ApiResponse<List<PeopleDto>> apiResponse = g.fromJson(responseBody, new TypeToken<ApiResponse<List<PeopleDto>>>() {}.getType());

                List<PersonWithPhone> pl = (apiResponse != null && apiResponse.getData() != null)
                        ? apiResponse.getData().stream()
                          .map(d -> PersonWithPhone.builder()
                                    .person(Person.builder()
                                            .name(d.getName())
                                            .build())
                                    .phoneNumber(d.getPhone()) // map corresponding fields
                                    .build())
                          .collect(Collectors.toList()) // or .collect(Collectors.toList()) if Java < 16 / Android minSdk < 34
                        : Collections.emptyList();

                personList.addAll(pl);
                chipCount.setText("Count: " + String.valueOf(personList.size()));
                personAdapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Response: " + responseBody, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                if (!isAdded()) return;
                if (chipCount != null) chipCount.setText( uri +" " + errorMessage);
                Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleAddPersonForm() {
        if (cardAddPersonForm != null) {
            if (cardAddPersonForm.getVisibility() == View.VISIBLE) {
                resetForm();
            } else {
                resetForm();
                cardAddPersonForm.setVisibility(View.VISIBLE);
                if (etInlineFirstName != null) {
                    etInlineFirstName.requestFocus();
                }
            }
        }
    }

    private void startEditPerson(PersonWithPhone item) {
        if (item == null || item.getPerson() == null) return;
        editingPerson = item.getPerson();

        String name = editingPerson.getName() != null ? editingPerson.getName() : editingPerson.getTitle();
        if (name == null) name = "";

        String[] parts = name.split(" ", 2);
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        if (etInlineFirstName != null) etInlineFirstName.setText(firstName);
        if (etInlineLastName != null) etInlineLastName.setText(lastName);
        if (etInlineDescription != null) etInlineDescription.setText(editingPerson.getDescription() != null ? editingPerson.getDescription() : "");
        if (etInlinePhone != null) etInlinePhone.setText(item.getPhoneNumber() != null ? item.getPhoneNumber() : "");
        if (etInlineEmail != null) etInlineEmail.setText(item.getEmail() != null ? item.getEmail() : "");
        if (etInlineLink != null) etInlineLink.setText(item.getLink() != null ? item.getLink() : "");
        if (etInlineGroup != null) etInlineGroup.setText(item.getGroupName() != null ? item.getGroupName() : "");

        if (btnInlineSubmit != null) btnInlineSubmit.setText("Update Person");
        if (cardAddPersonForm != null) cardAddPersonForm.setVisibility(View.VISIBLE);

        if (etInlineFirstName != null) etInlineFirstName.requestFocus();
    }

    private void saveOrUpdatePerson() {
        String firstName = etInlineFirstName != null ? etInlineFirstName.getText().toString().trim() : "";
        String lastName = etInlineLastName != null ? etInlineLastName.getText().toString().trim() : "";
        String phone = etInlinePhone != null ? etInlinePhone.getText().toString().trim() : "";
        String description = etInlineDescription != null ? etInlineDescription.getText().toString().trim() : "";
        String email = etInlineEmail != null ? etInlineEmail.getText().toString().trim() : "";
        String link = etInlineLink != null ? etInlineLink.getText().toString().trim() : "";
        String group = etInlineGroup != null ? etInlineGroup.getText().toString().trim() : "";

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(getContext(), "Please enter a first name", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullName = (firstName + " " + lastName).trim();

        if (editingPerson != null) {
            editingPerson.setName(fullName);
            editingPerson.setTitle(fullName);
            editingPerson.setDescription(description);

            DataLoader.updatePersonFull(requireContext(), editingPerson, phone, email, link, group, () -> {
                if (isAdded() && getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        loadPersons();
                        resetForm();
                        Toast.makeText(getContext(), fullName + " updated successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            Person newPerson = Person.builder()
                    .name(fullName)
                    .title(fullName)
                    .description(description)
                    .status(true)
                    .build();

            DataLoader.savePersonFull(requireContext(), newPerson, phone, email, link, group, () -> {
                if (isAdded() && getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        loadPersons();
                        resetForm();
                        Toast.makeText(getContext(), fullName + " saved successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    private void deletePerson(PersonWithPhone item) {
        if (item == null || item.getPerson() == null || item.getPerson().getPeople_id() == null) return;
        long personId = item.getPerson().getPeople_id();

        DataLoader.softDeletePerson(requireContext(), personId, () -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    loadPersons();
                    Toast.makeText(getContext(), "Person deleted", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void restorePerson(PersonWithPhone item) {
        if (item == null || item.getPerson() == null || item.getPerson().getPeople_id() == null) return;
        long personId = item.getPerson().getPeople_id();

        DataLoader.restorePerson(requireContext(), personId, () -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    loadPersons();
                    Toast.makeText(getContext(), "Person restored!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadPersons() {
        boolean includeDeleted = chipShowDeleted != null && chipShowDeleted.isChecked();
        DataLoader.loadPersonsWithPhones(requireContext(), includeDeleted, list -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    personList.clear();
                    personList.addAll(list);
                    if (personAdapter != null) {
                        personAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void resetForm() {
        editingPerson = null;
        if (etInlineFirstName != null) etInlineFirstName.setText("");
        if (etInlineLastName != null) etInlineLastName.setText("");
        if (etInlinePhone != null) etInlinePhone.setText("");
        if (etInlineDescription != null) etInlineDescription.setText("");
        if (etInlineEmail != null) etInlineEmail.setText("");
        if (etInlineLink != null) etInlineLink.setText("");
        if (etInlineGroup != null) etInlineGroup.setText("");
        if (btnInlineSubmit != null) btnInlineSubmit.setText("Save Person");
        if (cardAddPersonForm != null) cardAddPersonForm.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        chipAddBtn = null;
        cardAddPersonForm = null;
        etInlineFirstName = null;
        etInlineLastName = null;
        etInlinePhone = null;
        etInlineDescription = null;
        etInlineEmail = null;
        etInlineLink = null;
        etInlineGroup = null;
        btnInlineSubmit = null;
        chipCount = null;
        chipShowDeleted = null;
        chipShowCloudPerson = null;
    }
}
