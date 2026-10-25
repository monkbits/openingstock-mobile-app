package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Email;

import java.util.List;

@Dao
public interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Email email);

    @Update
    void update(Email email);

    @Query("SELECT * FROM email WHERE id = :id")
    Email getEmailById(long id);

    @Query("SELECT * FROM email WHERE title = :emailAddress LIMIT 1")
    Email getEmailByAddress(String emailAddress);

    @Query("SELECT * FROM email WHERE status = 1 OR status IS NULL")
    List<Email> getActiveEmails();

    @Query("SELECT * FROM email")
    List<Email> getAllEmailsIncludingDeleted();

    @Query("UPDATE email SET status = :status WHERE id = :emailId")
    void updateEmailStatus(long emailId, boolean status);
}
