package com.mymonkmindset.openingstock.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.models.Reminder;
import com.mymonkmindset.openingstock.models.ReminderWithPerson;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public interface OnReminderActionListener {
        void onEdit(ReminderWithPerson item);
        void onDelete(ReminderWithPerson item);
        void onRestore(ReminderWithPerson item);
        void onToggleComplete(ReminderWithPerson item, boolean isCompleted);
    }

    private final List<ReminderWithPerson> reminderList;
    private final OnReminderActionListener actionListener;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

    public ReminderAdapter(List<ReminderWithPerson> reminderList, OnReminderActionListener actionListener) {
        this.reminderList = reminderList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_card, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderWithPerson item = reminderList.get(position);
        Reminder reminder = item.getReminder();

        String title = reminder != null && reminder.getTitle() != null ? reminder.getTitle() : "Untitled Reminder";
        holder.tvReminderTitle.setText(title);

        String desc = reminder != null ? reminder.getDescription() : null;
        if (desc != null && !desc.trim().isEmpty()) {
            holder.tvReminderDescription.setVisibility(View.VISIBLE);
            holder.tvReminderDescription.setText(desc);
        } else {
            holder.tvReminderDescription.setVisibility(View.GONE);
        }

        if (reminder != null && reminder.getDueDate() != null) {
            holder.tvReminderDueDate.setText("Due: " + reminder.getDueDate().format(DATE_FORMATTER));
            holder.tvReminderDueDate.setVisibility(View.VISIBLE);
        } else {
            holder.tvReminderDueDate.setVisibility(View.GONE);
        }

        String remindBefore = reminder != null ? reminder.getRemindBefore() : null;
        if (remindBefore != null && !remindBefore.trim().isEmpty()) {
            holder.tvReminderRemindBefore.setText("Remind: " + remindBefore);
            holder.tvReminderRemindBefore.setVisibility(View.VISIBLE);
        } else {
            holder.tvReminderRemindBefore.setVisibility(View.GONE);
        }

        boolean isCompleted = reminder != null && Boolean.TRUE.equals(reminder.getIsCompleted());
        holder.cbReminderCompleted.setOnCheckedChangeListener(null);
        holder.cbReminderCompleted.setChecked(isCompleted);

        if (isCompleted) {
            holder.tvReminderTitle.setPaintFlags(holder.tvReminderTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvReminderTitle.setAlpha(0.5f);
        } else {
            holder.tvReminderTitle.setPaintFlags(holder.tvReminderTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvReminderTitle.setAlpha(1.0f);
        }

        boolean isDeleted = reminder != null && Boolean.FALSE.equals(reminder.getStatus());

        if (isDeleted) {
            holder.tvDeletedBadge.setVisibility(View.VISIBLE);
            holder.tvReminderTitle.setAlpha(0.5f);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnRestore.setVisibility(View.VISIBLE);
            holder.cbReminderCompleted.setEnabled(false);
        } else {
            holder.tvDeletedBadge.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnRestore.setVisibility(View.GONE);
            holder.cbReminderCompleted.setEnabled(true);
        }

        holder.cbReminderCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (actionListener != null) actionListener.onToggleComplete(item, isChecked);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onEdit(item);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onDelete(item);
        });

        holder.btnRestore.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onRestore(item);
        });
    }

    @Override
    public int getItemCount() {
        return reminderList != null ? reminderList.size() : 0;
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbReminderCompleted;
        TextView tvReminderTitle, tvReminderDescription, tvReminderDueDate, tvReminderRemindBefore, tvDeletedBadge;
        MaterialButton btnEdit, btnDelete, btnRestore;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            cbReminderCompleted = itemView.findViewById(R.id.cbReminderCompleted);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvReminderDescription = itemView.findViewById(R.id.tvReminderDescription);
            tvReminderDueDate = itemView.findViewById(R.id.tvReminderDueDate);
            tvReminderRemindBefore = itemView.findViewById(R.id.tvReminderRemindBefore);
            tvDeletedBadge = itemView.findViewById(R.id.tvDeletedBadge);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnRestore = itemView.findViewById(R.id.btnRestore);
        }
    }
}
