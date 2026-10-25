package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Link;
import com.mymonkmindset.openingstock.models.PersonLinkMap;

import java.util.List;

@Dao
public interface PersonLinkMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonLinkMap map);

    @Update
    void update(PersonLinkMap map);

    @Query("SELECT * FROM person_link_map WHERE personId = :personId")
    List<PersonLinkMap> getMappingsForPerson(long personId);

    @Query("SELECT l.* FROM link l INNER JOIN person_link_map m ON l.id = m.linkId WHERE m.personId = :personId LIMIT 1")
    Link getLinkForPerson(long personId);

    @Query("UPDATE person_link_map SET status = :status WHERE personId = :personId")
    void updateMapStatusForPerson(long personId, boolean status);
}
