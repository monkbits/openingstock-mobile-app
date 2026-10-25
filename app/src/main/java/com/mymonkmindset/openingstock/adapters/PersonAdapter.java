package com.mymonkmindset.openingstock.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.models.Person;
import com.mymonkmindset.openingstock.models.PersonWithPhone;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    public interface OnPersonActionListener {
        void onEdit(PersonWithPhone item);
        void onDelete(PersonWithPhone item);
        void onRestore(PersonWithPhone item);
    }

    private final List<PersonWithPhone> personList;
    private final OnPersonActionListener actionListener;

    public PersonAdapter(List<PersonWithPhone> personList, OnPersonActionListener actionListener) {
        this.personList = personList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_card, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        PersonWithPhone item = personList.get(position);
        Person person = item.getPerson();

        String displayName = person != null && person.getName() != null && !person.getName().isEmpty()
                ? person.getName()
                : (person != null ? person.getTitle() : "Unnamed");

        holder.tvPersonName.setText(displayName);

        // Description
        String desc = person != null ? person.getDescription() : null;
        if (desc != null && !desc.trim().isEmpty()) {
            holder.tvPersonDescription.setVisibility(View.VISIBLE);
            holder.tvPersonDescription.setText(desc);
        } else {
            holder.tvPersonDescription.setVisibility(View.GONE);
        }

        // Phone
        String phone = item.getPhoneNumber();
        if (phone != null && !phone.trim().isEmpty()) {
            holder.tvPersonPhone.setVisibility(View.VISIBLE);
            holder.tvPersonPhone.setText("Phone: " + phone);
        } else {
            holder.tvPersonPhone.setVisibility(View.GONE);
        }

        // Email
        String email = item.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            holder.tvPersonEmail.setVisibility(View.VISIBLE);
            holder.tvPersonEmail.setText("Email: " + email);
        } else {
            holder.tvPersonEmail.setVisibility(View.GONE);
        }

        // Link
        String link = item.getLink();
        if (link != null && !link.trim().isEmpty()) {
            holder.tvPersonLink.setVisibility(View.VISIBLE);
            holder.tvPersonLink.setText("Link: " + link);
        } else {
            holder.tvPersonLink.setVisibility(View.GONE);
        }

        // Group
        String group = item.getGroupName();
        if (group != null && !group.trim().isEmpty()) {
            holder.tvPersonGroup.setVisibility(View.VISIBLE);
            holder.tvPersonGroup.setText(group);
        } else {
            holder.tvPersonGroup.setVisibility(View.GONE);
        }

        boolean isDeleted = person != null && Boolean.FALSE.equals(person.getStatus());

        if (isDeleted) {
            holder.tvDeletedBadge.setVisibility(View.VISIBLE);
            holder.tvPersonName.setPaintFlags(holder.tvPersonName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvPersonName.setAlpha(0.6f);
            holder.tvPersonPhone.setAlpha(0.6f);

            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnRestore.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeletedBadge.setVisibility(View.GONE);
            holder.tvPersonName.setPaintFlags(holder.tvPersonName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvPersonName.setAlpha(1.0f);
            holder.tvPersonPhone.setAlpha(1.0f);

            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnRestore.setVisibility(View.GONE);
        }

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
        return personList != null ? personList.size() : 0;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvPersonName, tvPersonDescription, tvPersonPhone, tvPersonEmail, tvPersonLink, tvPersonGroup, tvDeletedBadge;
        MaterialButton btnEdit, btnDelete, btnRestore;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPersonName = itemView.findViewById(R.id.tvPersonName);
            tvPersonDescription = itemView.findViewById(R.id.tvPersonDescription);
            tvPersonPhone = itemView.findViewById(R.id.tvPersonPhone);
            tvPersonEmail = itemView.findViewById(R.id.tvPersonEmail);
            tvPersonLink = itemView.findViewById(R.id.tvPersonLink);
            tvPersonGroup = itemView.findViewById(R.id.tvPersonGroup);
            tvDeletedBadge = itemView.findViewById(R.id.tvDeletedBadge);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnRestore = itemView.findViewById(R.id.btnRestore);
        }
    }
}
