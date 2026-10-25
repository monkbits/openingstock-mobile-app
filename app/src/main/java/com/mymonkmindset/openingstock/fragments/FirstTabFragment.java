package com.mymonkmindset.openingstock.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.adapters.PersonAdapter;
import com.mymonkmindset.openingstock.interfaces.HandlesUi;
import com.mymonkmindset.openingstock.models.Person;
import com.mymonkmindset.openingstock.models.PersonWithPhone;
import com.mymonkmindset.openingstock.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class FirstTabFragment extends Fragment implements HandlesUi {

    private MaterialCardView addbtn, cardAddPersonForm;
    private EditText etInlineFirstName, etInlineLastName, etInlinePhone, etInlineDescription, etInlineEmail, etInlineLink, etInlineGroup;
    private Button btnInlineSubmit;
    private Chip chipShowDeleted;
    private RecyclerView rvCardsList;

    private List<PersonWithPhone> personList;
    private PersonAdapter personAdapter;
    private Person editingPerson = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeRecycleView(view);
        gatherResources();
        setOnClick();
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
            addbtn = getView().findViewById(R.id.addPersonBtn);
            cardAddPersonForm = getView().findViewById(R.id.cardAddPersonForm);
            etInlineFirstName = getView().findViewById(R.id.etInlineFirstName);
            etInlineLastName = getView().findViewById(R.id.etInlineLastName);
            etInlinePhone = getView().findViewById(R.id.etInlinePhone);
            etInlineDescription = getView().findViewById(R.id.etInlineDescription);
            etInlineEmail = getView().findViewById(R.id.etInlineEmail);
            etInlineLink = getView().findViewById(R.id.etInlineLink);
            etInlineGroup = getView().findViewById(R.id.etInlineGroup);
            btnInlineSubmit = getView().findViewById(R.id.btnInlineSubmit);
            chipShowDeleted = getView().findViewById(R.id.chipShowDeleted);
        }
    }

    @Override
    public void setOnClick() {
        if (addbtn != null) {
            addbtn.setOnClickListener(v -> toggleAddPersonForm());
        }

        if (btnInlineSubmit != null) {
            btnInlineSubmit.setOnClickListener(v -> saveOrUpdatePerson());
        }

        if (chipShowDeleted != null) {
            chipShowDeleted.setOnCheckedChangeListener((buttonView, isChecked) -> loadPersons());
        }
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
        addbtn = null;
        cardAddPersonForm = null;
        etInlineFirstName = null;
        etInlineLastName = null;
        etInlinePhone = null;
        etInlineDescription = null;
        etInlineEmail = null;
        etInlineLink = null;
        etInlineGroup = null;
        btnInlineSubmit = null;
        chipShowDeleted = null;
    }
}
