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
    tableName = "person_link_map",
    foreignKeys = {
        @ForeignKey(
            entity = Person.class,
            parentColumns = "people_id",
            childColumns = "personId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Link.class,
            parentColumns = "id",
            childColumns = "linkId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("personId"),
        @Index("linkId")
    }
)
public class PersonLinkMap {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long personId;
    private Long linkId;
    private String mappedOn;

    @Builder.Default
    private Boolean status = true;
}
