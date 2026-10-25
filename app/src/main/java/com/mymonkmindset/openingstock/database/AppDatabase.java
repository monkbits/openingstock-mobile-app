package com.mymonkmindset.openingstock.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mymonkmindset.openingstock.dao.EmailDao;
import com.mymonkmindset.openingstock.dao.GroupDao;
import com.mymonkmindset.openingstock.dao.LinkDao;
import com.mymonkmindset.openingstock.dao.PersonDao;
import com.mymonkmindset.openingstock.dao.PersonEmailMapDao;
import com.mymonkmindset.openingstock.dao.PersonGroupMapDao;
import com.mymonkmindset.openingstock.dao.PersonLinkMapDao;
import com.mymonkmindset.openingstock.dao.PersonPhoneMapDao;
import com.mymonkmindset.openingstock.dao.PersonReminderMapDao;
import com.mymonkmindset.openingstock.dao.PhoneDao;
import com.mymonkmindset.openingstock.dao.ReminderDao;
import com.mymonkmindset.openingstock.models.Email;
import com.mymonkmindset.openingstock.models.Group;
import com.mymonkmindset.openingstock.models.Link;
import com.mymonkmindset.openingstock.models.Person;
import com.mymonkmindset.openingstock.models.PersonEmailMap;
import com.mymonkmindset.openingstock.models.PersonGroupMap;
import com.mymonkmindset.openingstock.models.PersonLinkMap;
import com.mymonkmindset.openingstock.models.PersonPhoneMap;
import com.mymonkmindset.openingstock.models.PersonReminderMap;
import com.mymonkmindset.openingstock.models.Phone;
import com.mymonkmindset.openingstock.models.Reminder;

@Database(
    entities = {
        Person.class,
        Phone.class,
        PersonPhoneMap.class,
        Group.class,
        PersonGroupMap.class,
        Email.class,
        PersonEmailMap.class,
        Link.class,
        PersonLinkMap.class,
        Reminder.class,
        PersonReminderMap.class
    },
    version = 4,
    exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
    public abstract PhoneDao phoneDao();
    public abstract PersonPhoneMapDao personPhoneMapDao();
    public abstract GroupDao groupDao();
    public abstract PersonGroupMapDao personGroupMapDao();
    public abstract EmailDao emailDao();
    public abstract PersonEmailMapDao personEmailMapDao();
    public abstract LinkDao linkDao();
    public abstract PersonLinkMapDao personLinkMapDao();
    public abstract ReminderDao reminderDao();
    public abstract PersonReminderMapDao personReminderMapDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_offline_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
