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
    tableName = "person_reminder_map",
    foreignKeys = {
        @ForeignKey(
            entity = Person.class,
            parentColumns = "people_id",
            childColumns = "personId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Reminder.class,
            parentColumns = "id",
            childColumns = "reminderId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("personId"),
        @Index("reminderId")
    }
)
public class PersonReminderMap {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long personId;
    private Long reminderId;
    private String mappedOn;

    @Builder.Default
    private Boolean status = true;
}
