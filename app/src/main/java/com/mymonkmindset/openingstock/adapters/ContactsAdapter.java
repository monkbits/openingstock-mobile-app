package com.mymonkmindset.openingstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.models.ContactItem;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private final List<ContactItem> contactItemList;

    public ContactsAdapter(List<ContactItem> contactItemList) {
        this.contactItemList = contactItemList;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvContactName, tvContactNumber, tvAccountBadge, tvAvtarInitials;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvContactNumber = itemView.findViewById(R.id.tvContactNumber);
            tvAccountBadge = itemView.findViewById(R.id.tvAccountBadge);
            tvAvtarInitials = itemView.findViewById(R.id.tvAvtarInitials);
        }
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactViewHolder holder, int position) {
        ContactItem item = contactItemList.get(position);
        holder.tvContactName.setText(item.getName());
        holder.tvContactNumber.setText(item.getNumber());
        holder.tvAccountBadge.setText(item.getAccountType());
        if (holder.tvAvtarInitials != null && item.getName() != null && !item.getName().isEmpty()) {
            holder.tvAvtarInitials.setText(item.getName().substring(0, 1).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return contactItemList != null ? contactItemList.size() : 0;
    }
}
