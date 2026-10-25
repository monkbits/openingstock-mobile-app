package com.mymonkmindset.openingstock.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.adapters.FileAdapter;
import com.mymonkmindset.openingstock.models.FileItem;

import java.util.ArrayList;
import java.util.List;

public class FileManagerActivity extends BaseActivity {

    private RecyclerView rvFiles;
    private TextView tvCurrentPath, tvStorageUsage;
    private FileAdapter fileAdapter;
    private List<FileItem> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_file_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prepareParent(this);
        initViews();
        loadFiles();
    }

    private void initViews() {
        rvFiles = findViewById(R.id.rvFiles);
        tvCurrentPath = findViewById(R.id.tvCurrentPath);
        tvStorageUsage = findViewById(R.id.tvStorageUsage);

        if (tvCurrentPath != null) {
            tvCurrentPath.setText("📁 / Internal Storage / OpeningStock");
        }
        if (tvStorageUsage != null) {
            tvStorageUsage.setText("48.5 GB used of 128 GB");
        }
    }

    private void loadFiles() {
        if (rvFiles == null) return;

        fileList = new ArrayList<>();
        fileList.add(new FileItem("Documents", "/OpeningStock/Documents", "--", "folder", "8 files • May 2025", true));
        fileList.add(new FileItem("Images & Scans", "/OpeningStock/Images", "--", "folder", "24 files • May 2025", true));
        fileList.add(new FileItem("Voice Notes", "/OpeningStock/Audio", "--", "folder", "5 items • May 2025", true));
        fileList.add(new FileItem("Opening_Stock_Report.pdf", "/OpeningStock/Report.pdf", "2.4 MB", "pdf", "May 10, 2025", false));
        fileList.add(new FileItem("Product_Inventory.csv", "/OpeningStock/Inventory.csv", "850 KB", "code", "May 09, 2025", false));
        fileList.add(new FileItem("App_Backup_2025.zip", "/OpeningStock/Backup.zip", "14.2 MB", "zip", "May 08, 2025", false));
        fileList.add(new FileItem("Invoice_Receipt.jpg", "/OpeningStock/Invoice.jpg", "1.8 MB", "image", "May 07, 2025", false));

        rvFiles.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(fileList, item -> {
            if (item.isFolder()) {
                if (tvCurrentPath != null) {
                    tvCurrentPath.setText("📁 " + item.getPath());
                }
                Toast.makeText(FileManagerActivity.this, "Opening folder: " + item.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FileManagerActivity.this, "Opening file: " + item.getName() + " (" + item.getSize() + ")", Toast.LENGTH_SHORT).show();
            }
        });
        rvFiles.setAdapter(fileAdapter);
    }
}
