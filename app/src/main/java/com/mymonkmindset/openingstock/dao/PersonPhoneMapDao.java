package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.PersonPhoneMap;
import com.mymonkmindset.openingstock.models.Phone;

import java.util.List;

@Dao
public interface PersonPhoneMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonPhoneMap personPhoneMap);

    @Update
    void update(PersonPhoneMap personPhoneMap);

    @Query("SELECT * FROM person_phone_map WHERE personId = :personId")
    List<PersonPhoneMap> getMappingsForPerson(long personId);

    @Query("SELECT p.* FROM phone p INNER JOIN person_phone_map m ON p.id = m.phoneId WHERE m.personId = :personId AND (m.status = 1 OR m.status IS NULL) AND (p.status = 1 OR p.status IS NULL) LIMIT 1")
    Phone getActivePhoneForPerson(long personId);

    @Query("SELECT p.* FROM phone p INNER JOIN person_phone_map m ON p.id = m.phoneId WHERE m.personId = :personId LIMIT 1")
    Phone getPhoneForPerson(long personId);

    @Query("UPDATE person_phone_map SET status = :status WHERE personId = :personId")
    void updateMapStatusForPerson(long personId, boolean status);
}
