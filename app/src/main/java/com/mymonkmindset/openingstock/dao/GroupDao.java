package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Group;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Group group);

    @Update
    void update(Group group);

    @Query("SELECT * FROM group_table WHERE id = :id")
    Group getGroupById(long id);

    @Query("SELECT * FROM group_table WHERE title = :title LIMIT 1")
    Group getGroupByTitle(String title);

    @Query("SELECT * FROM group_table WHERE status = 1 OR status IS NULL")
    List<Group> getActiveGroups();

    @Query("SELECT * FROM group_table")
    List<Group> getAllGroupsIncludingDeleted();

    @Query("UPDATE group_table SET status = :status WHERE id = :groupId")
    void updateGroupStatus(long groupId, boolean status);
}
