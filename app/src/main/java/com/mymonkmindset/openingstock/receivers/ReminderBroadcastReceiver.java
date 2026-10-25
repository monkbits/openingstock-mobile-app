package com.mymonkmindset.openingstock.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.activities.MainActivity;
import com.mymonkmindset.openingstock.utils.ReminderNotificationHelper;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;

        long reminderId = intent.getLongExtra("reminder_id", 0);
        String title = intent.getStringExtra("reminder_title");
        String desc = intent.getStringExtra("reminder_desc");

        if (title == null || title.isEmpty()) {
            title = "Scheduled Reminder";
        }
        if (desc == null || desc.isEmpty()) {
            desc = "You have an upcoming reminder due now.";
        }

        ReminderNotificationHelper.createNotificationChannel(context);

        Intent openAppIntent = new Intent(context, MainActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) reminderId, openAppIntent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ReminderNotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_opening_stock)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) reminderId, builder.build());
    }
}
