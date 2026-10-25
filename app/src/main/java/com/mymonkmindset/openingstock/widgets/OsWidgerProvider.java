package com.mymonkmindset.openingstock.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.recyclerview.widget.RecyclerView;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.activities.MainActivity;

public class OsWidgerProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Iterate over all active instances of this widget
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // 1. Build RemoteViews targeting the layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_opening_stock);

        // 2. Set dynamic text (can be pulled from SharedPreferences or Room DB)
//        views.setTextViewText(R.id.tvWidgetQuote, "“Do not let the behavior of others destroy your inner peace.”");
        views.setTextViewText(R.id.tvWidgetStreak, "🔥 14d");

        // 3. Make clicking the widget open MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent);

        // 4. Instruct the manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
