package com.mymonkmindset.openingstock.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mymonkmindset.openingstock.interfaces.Listable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(tableName = "email")
public class Email implements Listable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private Boolean verified;
    private String createdOn;

    @Builder.Default
    private Boolean status = true;
}
