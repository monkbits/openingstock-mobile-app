package com.mymonkmindset.openingstock.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileItem {
    private String name;
    private String path;
    private String size;
    private String type; // "folder", "pdf", "image", "audio", "code", "zip", "doc"
    private String details; // e.g. "12 items" or "May 10, 2025"
    private boolean isFolder;
}
