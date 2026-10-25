package com.mymonkmindset.openingstock.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.mymonkmindset.openingstock.adapters.ReminderAdapter;
import com.mymonkmindset.openingstock.interfaces.HandlesUi;
import com.mymonkmindset.openingstock.models.Reminder;
import com.mymonkmindset.openingstock.models.ReminderWithPerson;
import com.mymonkmindset.openingstock.utils.DataLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderTabFragment extends Fragment implements HandlesUi {

    private MaterialCardView addReminderBtn, cardAddReminderForm;
    private EditText etReminderTitle, etReminderDescription, etReminderDueDate, etReminderPerson;
    private AutoCompleteTextView actvRemindBefore;
    private Button btnReminderSubmit;
    private Chip chipShowDeleted;
    private RecyclerView rvReminderList;

    private List<ReminderWithPerson> reminderList;
    private ReminderAdapter reminderAdapter;
    private Reminder editingReminder = null;
    private LocalDateTime selectedDueDate = null;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeRecyclerView(view);
        gatherResources();
        setupRemindBeforeDropdown();
        setOnClick();
    }

    private void makeRecyclerView(@NonNull View view) {
        rvReminderList = view.findViewById(R.id.rvReminderList);
        if (rvReminderList != null) {
            reminderList = new ArrayList<>();
            rvReminderList.setLayoutManager(new LinearLayoutManager(requireContext()));
            reminderAdapter = new ReminderAdapter(reminderList, new ReminderAdapter.OnReminderActionListener() {
                @Override
                public void onEdit(ReminderWithPerson item) {
                    startEditReminder(item);
                }

                @Override
                public void onDelete(ReminderWithPerson item) {
                    deleteReminder(item);
                }

                @Override
                public void onRestore(ReminderWithPerson item) {
                    restoreReminder(item);
                }

                @Override
                public void onToggleComplete(ReminderWithPerson item, boolean isCompleted) {
                    toggleCompleteReminder(item, isCompleted);
                }
            });
            rvReminderList.setAdapter(reminderAdapter);
            loadReminders();
        }
    }

    @Override
    public void gatherResources() {
        if (getView() != null) {
            addReminderBtn = getView().findViewById(R.id.addReminderBtn);
            cardAddReminderForm = getView().findViewById(R.id.cardAddReminderForm);
            etReminderTitle = getView().findViewById(R.id.etReminderTitle);
            etReminderDescription = getView().findViewById(R.id.etReminderDescription);
            etReminderDueDate = getView().findViewById(R.id.etReminderDueDate);
            actvRemindBefore = getView().findViewById(R.id.actvRemindBefore);
            etReminderPerson = getView().findViewById(R.id.etReminderPerson);
            btnReminderSubmit = getView().findViewById(R.id.btnReminderSubmit);
            chipShowDeleted = getView().findViewById(R.id.chipShowDeleted);
        }
    }

    private void setupRemindBeforeDropdown() {
        if (actvRemindBefore != null) {
            String[] options = new String[]{
                    "At time of event",
                    "10 minutes before",
                    "30 minutes before",
                    "1 hour before",
                    "1 day before"
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, options);
            actvRemindBefore.setAdapter(adapter);
        }
    }

    @Override
    public void setOnClick() {
        if (addReminderBtn != null) {
            addReminderBtn.setOnClickListener(v -> toggleAddReminderForm());
        }

        if (btnReminderSubmit != null) {
            btnReminderSubmit.setOnClickListener(v -> saveOrUpdateReminder());
        }

        if (etReminderDueDate != null) {
            etReminderDueDate.setOnClickListener(v -> showDateTimePicker());
        }

        if (chipShowDeleted != null) {
            chipShowDeleted.setOnCheckedChangeListener((buttonView, isChecked) -> loadReminders());
        }
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timeView, hourOfDay, minute) -> {
                selectedDueDate = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute);
                if (etReminderDueDate != null) {
                    etReminderDueDate.setText(selectedDueDate.format(DATE_FORMATTER));
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void toggleAddReminderForm() {
        if (cardAddReminderForm != null) {
            if (cardAddReminderForm.getVisibility() == View.VISIBLE) {
                resetForm();
            } else {
                resetForm();
                cardAddReminderForm.setVisibility(View.VISIBLE);
                if (etReminderTitle != null) {
                    etReminderTitle.requestFocus();
                }
            }
        }
    }

    private void startEditReminder(ReminderWithPerson item) {
        if (item == null || item.getReminder() == null) return;
        editingReminder = item.getReminder();

        if (etReminderTitle != null) etReminderTitle.setText(editingReminder.getTitle() != null ? editingReminder.getTitle() : "");
        if (etReminderDescription != null) etReminderDescription.setText(editingReminder.getDescription() != null ? editingReminder.getDescription() : "");

        selectedDueDate = editingReminder.getDueDate();
        if (etReminderDueDate != null && selectedDueDate != null) {
            etReminderDueDate.setText(selectedDueDate.format(DATE_FORMATTER));
        }

        if (actvRemindBefore != null) actvRemindBefore.setText(editingReminder.getRemindBefore() != null ? editingReminder.getRemindBefore() : "", false);
        if (etReminderPerson != null) etReminderPerson.setText(item.getPersonName() != null ? item.getPersonName() : "");

        if (btnReminderSubmit != null) btnReminderSubmit.setText("Update Reminder");
        if (cardAddReminderForm != null) cardAddReminderForm.setVisibility(View.VISIBLE);

        if (etReminderTitle != null) etReminderTitle.requestFocus();
    }

    private void saveOrUpdateReminder() {
        String title = etReminderTitle != null ? etReminderTitle.getText().toString().trim() : "";
        String desc = etReminderDescription != null ? etReminderDescription.getText().toString().trim() : "";
        String remindBefore = actvRemindBefore != null ? actvRemindBefore.getText().toString().trim() : "";
        String personName = etReminderPerson != null ? etReminderPerson.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getContext(), "Please enter a reminder title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingReminder != null) {
            editingReminder.setTitle(title);
            editingReminder.setDescription(desc);
            editingReminder.setDueDate(selectedDueDate);
            editingReminder.setRemindBefore(remindBefore);

            DataLoader.updateReminder(requireContext(), editingReminder, () -> {
                if (isAdded() && getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        loadReminders();
                        resetForm();
                        Toast.makeText(getContext(), title + " updated successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            Reminder newReminder = Reminder.builder()
                    .title(title)
                    .description(desc)
                    .dueDate(selectedDueDate)
                    .remindBefore(remindBefore)
                    .isCompleted(false)
                    .status(true)
                    .createdOn(String.valueOf(System.currentTimeMillis()))
                    .build();

            DataLoader.saveReminder(requireContext(), newReminder, personName, () -> {
                if (isAdded() && getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        loadReminders();
                        resetForm();
                        Toast.makeText(getContext(), title + " saved successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    private void deleteReminder(ReminderWithPerson item) {
        if (item == null || item.getReminder() == null || item.getReminder().getId() == null) return;
        long reminderId = item.getReminder().getId();

        DataLoader.softDeleteReminder(requireContext(), reminderId, () -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    loadReminders();
                    Toast.makeText(getContext(), "Reminder deleted", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void restoreReminder(ReminderWithPerson item) {
        if (item == null || item.getReminder() == null || item.getReminder().getId() == null) return;
        long reminderId = item.getReminder().getId();

        DataLoader.restoreReminder(requireContext(), reminderId, () -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    loadReminders();
                    Toast.makeText(getContext(), "Reminder restored!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void toggleCompleteReminder(ReminderWithPerson item, boolean isCompleted) {
        if (item == null || item.getReminder() == null || item.getReminder().getId() == null) return;
        long reminderId = item.getReminder().getId();

        DataLoader.toggleReminderCompletion(requireContext(), reminderId, isCompleted, () -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    if (item.getReminder() != null) {
                        item.getReminder().setIsCompleted(isCompleted);
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadReminders() {
        boolean includeDeleted = chipShowDeleted != null && chipShowDeleted.isChecked();
        DataLoader.loadReminders(requireContext(), includeDeleted, list -> {
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> {
                    reminderList.clear();
                    reminderList.addAll(list);
                    if (reminderAdapter != null) {
                        reminderAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void resetForm() {
        editingReminder = null;
        selectedDueDate = null;
        if (etReminderTitle != null) etReminderTitle.setText("");
        if (etReminderDescription != null) etReminderDescription.setText("");
        if (etReminderDueDate != null) etReminderDueDate.setText("");
        if (actvRemindBefore != null) actvRemindBefore.setText("", false);
        if (etReminderPerson != null) etReminderPerson.setText("");
        if (btnReminderSubmit != null) btnReminderSubmit.setText("Save Reminder");
        if (cardAddReminderForm != null) cardAddReminderForm.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addReminderBtn = null;
        cardAddReminderForm = null;
        etReminderTitle = null;
        etReminderDescription = null;
        etReminderDueDate = null;
        actvRemindBefore = null;
        etReminderPerson = null;
        btnReminderSubmit = null;
        chipShowDeleted = null;
    }
}
