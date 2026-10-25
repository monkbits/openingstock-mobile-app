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
    tableName = "person_group_map",
    foreignKeys = {
        @ForeignKey(
            entity = Person.class,
            parentColumns = "people_id",
            childColumns = "personId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Group.class,
            parentColumns = "id",
            childColumns = "groupId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("personId"),
        @Index("groupId")
    }
)
public class PersonGroupMap {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long personId;
    private Long groupId;
    private String mappedOn;

    @Builder.Default
    private Boolean status = true;
}
