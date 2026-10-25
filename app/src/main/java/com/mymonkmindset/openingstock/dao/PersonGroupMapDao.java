package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Group;
import com.mymonkmindset.openingstock.models.PersonGroupMap;

import java.util.List;

@Dao
public interface PersonGroupMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PersonGroupMap map);

    @Update
    void update(PersonGroupMap map);

    @Query("SELECT * FROM person_group_map WHERE personId = :personId")
    List<PersonGroupMap> getMappingsForPerson(long personId);

    @Query("SELECT g.* FROM group_table g INNER JOIN person_group_map m ON g.id = m.groupId WHERE m.personId = :personId LIMIT 1")
    Group getGroupForPerson(long personId);

    @Query("UPDATE person_group_map SET status = :status WHERE personId = :personId")
    void updateMapStatusForPerson(long personId, boolean status);
}
