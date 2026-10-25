package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Email;
import com.mymonkmindset.openingstock.models.PersonEmailMap;

import java.util.List;

@Dao
public interface PersonEmailMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonEmailMap map);

    @Update
    void update(PersonEmailMap map);

    @Query("SELECT * FROM person_email_map WHERE personId = :personId")
    List<PersonEmailMap> getMappingsForPerson(long personId);

    @Query("SELECT e.* FROM email e INNER JOIN person_email_map m ON e.id = m.emailId WHERE m.personId = :personId LIMIT 1")
    Email getEmailForPerson(long personId);

    @Query("UPDATE person_email_map SET status = :status WHERE personId = :personId")
    void updateMapStatusForPerson(long personId, boolean status);
}
