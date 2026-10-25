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
    tableName = "person_phone_map",
    foreignKeys = {
        @ForeignKey(
            entity = Person.class,
            parentColumns = "people_id",
            childColumns = "personId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Phone.class,
            parentColumns = "id",
            childColumns = "phoneId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("personId"),
        @Index("phoneId")
    }
)
public class PersonPhoneMap {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long personId;
    private Long phoneId;
    private String mappedOn;
    private Boolean status;
}
