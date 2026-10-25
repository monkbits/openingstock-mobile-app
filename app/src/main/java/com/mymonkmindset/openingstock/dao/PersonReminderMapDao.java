package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.PersonReminderMap;
import com.mymonkmindset.openingstock.models.Reminder;

import java.util.List;

@Dao
public interface PersonReminderMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonReminderMap map);

    @Update
    void update(PersonReminderMap map);

    @Query("SELECT * FROM person_reminder_map WHERE personId = :personId")
    List<PersonReminderMap> getMappingsForPerson(long personId);

    @Query("SELECT r.* FROM reminder r INNER JOIN person_reminder_map m ON r.id = m.reminderId WHERE m.personId = :personId")
    List<Reminder> getRemindersForPerson(long personId);

    @Query("UPDATE person_reminder_map SET status = :status WHERE personId = :personId")
    void updateMapStatusForPerson(long personId, boolean status);
}
