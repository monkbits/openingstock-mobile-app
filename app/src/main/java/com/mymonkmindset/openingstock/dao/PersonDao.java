package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Person;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Person person);

    @Update
    void update(Person person);

    @Query("SELECT * FROM persons WHERE status = 1 OR status IS NULL ORDER BY people_id DESC")
    List<Person> getActivePersons();

    @Query("SELECT * FROM persons ORDER BY people_id DESC")
    List<Person> getAllPersonsIncludingDeleted();

    @Query("UPDATE persons SET status = :status WHERE people_id = :personId")
    void updatePersonStatus(long personId, boolean status);
}
