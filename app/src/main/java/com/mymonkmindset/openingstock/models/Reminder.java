package com.mymonkmindset.openingstock.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mymonkmindset.openingstock.interfaces.Listable;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(tableName = "reminder")
public class Reminder implements Listable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String remindBefore;

    @Builder.Default
    private Boolean isCompleted = false;

    @Builder.Default
    private Boolean status = true;

    private String createdOn;
}
