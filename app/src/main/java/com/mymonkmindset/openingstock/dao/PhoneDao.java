package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Phone;

import java.util.List;

@Dao
public interface PhoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Phone phone);

    @Update
    void update(Phone phone);

    @Query("SELECT * FROM phone WHERE id = :id")
    Phone getPhoneById(long id);

    @Query("SELECT * FROM phone WHERE title = :phoneNumber LIMIT 1")
    Phone getPhoneByNumber(String phoneNumber);

    @Query("SELECT * FROM phone WHERE status = 1 OR status IS NULL")
    List<Phone> getActivePhones();

    @Query("SELECT * FROM phone")
    List<Phone> getAllPhonesIncludingDeleted();

    @Query("UPDATE phone SET status = :status WHERE id = :phoneId")
    void updatePhoneStatus(long phoneId, boolean status);
}
