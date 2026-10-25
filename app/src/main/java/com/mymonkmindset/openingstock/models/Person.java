package com.mymonkmindset.openingstock.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mymonkmindset.openingstock.interfaces.Listable;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
@Entity(tableName = "persons")
public class Person implements Listable {
    @PrimaryKey(autoGenerate = true)
    private Long            people_id;
    private String          name;
    private String          title;
    private String          description;
    private LocalDateTime   dob;

    @Builder.Default
    private Boolean         status = true;
}
