package com.mymonkmindset.openingstock.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mymonkmindset.openingstock.models.Link;

import java.util.List;

@Dao
public interface LinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Link link);

    @Update
    void update(Link link);

    @Query("SELECT * FROM link WHERE id = :id")
    Link getLinkById(long id);

    @Query("SELECT * FROM link WHERE url = :url LIMIT 1")
    Link getLinkByUrl(String url);

    @Query("SELECT * FROM link WHERE status = 1 OR status IS NULL")
    List<Link> getActiveLinks();

    @Query("SELECT * FROM link")
    List<Link> getAllLinksIncludingDeleted();

    @Query("UPDATE link SET status = :status WHERE id = :linkId")
    void updateLinkStatus(long linkId, boolean status);
}
