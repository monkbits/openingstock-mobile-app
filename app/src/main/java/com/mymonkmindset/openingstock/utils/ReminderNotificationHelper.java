package com.mymonkmindset.openingstock.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.mymonkmindset.openingstock.models.Reminder;
import com.mymonkmindset.openingstock.receivers.ReminderBroadcastReceiver;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReminderNotificationHelper {

    public static final String CHANNEL_ID = "reminder_notification_channel";
    public static final String CHANNEL_NAME = "Reminder Notifications";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for upcoming scheduled reminders");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static long calculateTriggerMillis(LocalDateTime dueDate, String remindBefore) {
        if (dueDate == null) return -1;

        LocalDateTime triggerTime = dueDate;

        if (remindBefore != null) {
            String lower = remindBefore.toLowerCase();
            if (lower.contains("10 min")) {
                triggerTime = dueDate.minusMinutes(10);
            } else if (lower.contains("30 min")) {
                triggerTime = dueDate.minusMinutes(30);
            } else if (lower.contains("1 hour")) {
                triggerTime = dueDate.minusHours(1);
            } else if (lower.contains("1 day")) {
                triggerTime = dueDate.minusDays(1);
            }
        }

        return triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static void scheduleNotification(Context context, Reminder reminder) {
        if (reminder == null || reminder.getId() == null || reminder.getDueDate() == null) return;
        if (Boolean.TRUE.equals(reminder.getIsCompleted()) || Boolean.FALSE.equals(reminder.getStatus())) {
            cancelNotification(context, reminder.getId());
            return;
        }

        long triggerMillis = calculateTriggerMillis(reminder.getDueDate(), reminder.getRemindBefore());
        if (triggerMillis <= System.currentTimeMillis()) {
            return;
        }

        createNotificationChannel(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("reminder_id", reminder.getId());
        intent.putExtra("reminder_title", reminder.getTitle());
        intent.putExtra("reminder_desc", reminder.getDescription());

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.getId().intValue(),
                intent,
                flags
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);
        }
    }

    public static void cancelNotification(Context context, long reminderId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        int flags = PendingIntent.FLAG_NO_CREATE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) reminderId,
                intent,
                flags
        );

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
