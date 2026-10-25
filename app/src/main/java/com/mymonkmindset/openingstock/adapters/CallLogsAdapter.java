package com.mymonkmindset.openingstock.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.models.CallLogItem;

import java.util.List;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.CallLogViewHolder> {

    private final List<CallLogItem> callLogList;

    public CallLogsAdapter(List<CallLogItem> callLogList) {
        this.callLogList = callLogList;
    }

    public static class CallLogViewHolder extends RecyclerView.ViewHolder {
        TextView tvCallLogName, tvCallLogDetails, tvCallTypeBadge, tvCallLogTimestamp, tvCallLogInitials;

        public CallLogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCallLogName = itemView.findViewById(R.id.tvCallLogName);
            tvCallLogDetails = itemView.findViewById(R.id.tvCallLogDetails);
            tvCallTypeBadge = itemView.findViewById(R.id.tvCallTypeBadge);
            tvCallLogTimestamp = itemView.findViewById(R.id.tvCallLogTimestamp);
            tvCallLogInitials = itemView.findViewById(R.id.tvCallLogInitials);
        }
    }

    @NonNull
    @Override
    public CallLogsAdapter.CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_log, parent, false);
        return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogsAdapter.CallLogViewHolder holder, int position) {
        CallLogItem item = callLogList.get(position);
        holder.tvCallLogName.setText(item.getName());
        String details = item.getNumber() + " • " + item.getDuration();
        holder.tvCallLogDetails.setText(details);
        holder.tvCallTypeBadge.setText(item.getCallType());
        holder.tvCallLogTimestamp.setText(item.getTimestamp());

        if (item.getCallType() != null && item.getCallType().toLowerCase().contains("snapchat")) {
            holder.tvCallTypeBadge.setBackgroundColor(Color.parseColor("#FFFC00"));
            holder.tvCallTypeBadge.setTextColor(Color.parseColor("#121318"));
        } else {
            holder.tvCallTypeBadge.setBackgroundColor(Color.parseColor("#252833"));
            holder.tvCallTypeBadge.setTextColor(Color.parseColor("#E5A93B"));
        }

        if (holder.tvCallLogInitials != null && item.getName() != null && !item.getName().isEmpty()) {
            String initialName = item.getName().replace("Snapchat - ", "").trim();
            if (!initialName.isEmpty()) {
                holder.tvCallLogInitials.setText(initialName.substring(0, 1).toUpperCase());
            }
        }
    }

    @Override
    public int getItemCount() {
        return callLogList != null ? callLogList.size() : 0;
    }
}
