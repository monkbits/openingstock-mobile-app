package com.mymonkmindset.openingstock.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(
    tableName = "person_email_map",
    foreignKeys = {
        @ForeignKey(
            entity = Person.class,
            parentColumns = "people_id",
            childColumns = "personId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Email.class,
            parentColumns = "id",
            childColumns = "emailId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("personId"),
        @Index("emailId")
    }
)
public class PersonEmailMap {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long personId;
    private Long emailId;
    private String mappedOn;

    @Builder.Default
    private Boolean status = true;
}
