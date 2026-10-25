package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Query("SELECT * FROM reminder WHERE id = :id")
    Reminder getReminderById(long id);

    @Query("SELECT * FROM reminder WHERE status = 1 OR status IS NULL ORDER BY id DESC")
    List<Reminder> getActiveReminders();

    @Query("SELECT * FROM reminder ORDER BY id DESC")
    List<Reminder> getAllRemindersIncludingDeleted();

    @Query("UPDATE reminder SET status = :status WHERE id = :reminderId")
    void updateReminderStatus(long reminderId, boolean status);

    @Query("UPDATE reminder SET isCompleted = :completed WHERE id = :reminderId")
    void updateReminderCompletion(long reminderId, boolean completed);
}
