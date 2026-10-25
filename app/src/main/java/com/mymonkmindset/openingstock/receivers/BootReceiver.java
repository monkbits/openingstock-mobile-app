package com.mymonkmindset.openingstock.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mymonkmindset.openingstock.database.AppDatabase;
import com.mymonkmindset.openingstock.models.Reminder;
import com.mymonkmindset.openingstock.utils.DataLoader;
import com.mymonkmindset.openingstock.utils.ReminderNotificationHelper;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            DataLoader.diskIO.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                List<Reminder> activeReminders = db.reminderDao().getActiveReminders();
                for (Reminder reminder : activeReminders) {
                    ReminderNotificationHelper.scheduleNotification(context, reminder);
                }
            });
        }
    }
}
