package com.mymonkmindset.openingstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.models.FileItem;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    public interface OnFileClickListener {
        void onFileClick(FileItem item);
    }

    private final List<FileItem> fileList;
    private final OnFileClickListener clickListener;

    public FileAdapter(List<FileItem> fileList, OnFileClickListener clickListener) {
        this.fileList = fileList;
        this.clickListener = clickListener;
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName, tvFileDetails, tvFileType, tvFileBadge;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFileDetails = itemView.findViewById(R.id.tvFileDetails);
            tvFileType = itemView.findViewById(R.id.tvFileType);
            tvFileBadge = itemView.findViewById(R.id.tvFileBadge);
        }
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileItem item = fileList.get(position);
        holder.tvFileName.setText(item.getName());
        holder.tvFileDetails.setText(item.getDetails());
        holder.tvFileType.setText(item.getType().toUpperCase());

        String iconEmoji;
        switch (item.getType().toLowerCase()) {
            case "folder":
                iconEmoji = "📁";
                break;
            case "pdf":
                iconEmoji = "📄";
                break;
            case "image":
                iconEmoji = "🖼️";
                break;
            case "audio":
                iconEmoji = "🎵";
                break;
            case "code":
                iconEmoji = "💻";
                break;
            case "zip":
                iconEmoji = "📦";
                break;
            default:
                iconEmoji = "📝";
                break;
        }
        holder.tvFileBadge.setText(iconEmoji);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onFileClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList != null ? fileList.size() : 0;
    }
}
