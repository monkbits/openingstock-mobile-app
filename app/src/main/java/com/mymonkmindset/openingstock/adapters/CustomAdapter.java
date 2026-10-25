package com.mymonkmindset.openingstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.interfaces.Listable;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ListableViewHolder>{
    List<Listable> listItems;

    public CustomAdapter(List<Listable> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public CustomAdapter.ListableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent,false);
        return new CustomAdapter.ListableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ListableViewHolder holder, int position) {
        Listable p = listItems.get(position);
        holder.cardItem.setText(p.getTitle());
    }

    @Override
    public int getItemCount() {
        return listItems != null? listItems.size(): 0;
    }

    public static class ListableViewHolder extends RecyclerView.ViewHolder{
        TextView cardItem;
        public ListableViewHolder(@NonNull View itemView) {
            super(itemView);
            cardItem = itemView.findViewById(R.id.cardItem);
        }
    }
}
