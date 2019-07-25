package com.example.halima.talkingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

//import static com.example.halima.talkingapp.FragmentUsers.FULLNAME;
//import static com.example.halima.talkingapp.FragmentUsers.SHARED_PREFS;
//import static com.example.halima.talkingapp.FragmentUsers.USERNAME;
import static com.example.halima.talkingapp.MainActivity.FULLNAME;
import static com.example.halima.talkingapp.MainActivity.SHARED_PREFS;
import static com.example.halima.talkingapp.MainActivity.USERNAME;

/**
 * Implementation of App Widget functionality.
 */
public class UserInfoAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        /*CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);*/
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
           // updateAppWidget(context, appWidgetManager, appWidgetId);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    String fullname = prefs.getString(FULLNAME,"fullname");
                    String username = prefs.getString(USERNAME,"username");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setTextViewText(R.id.name, fullname);
            views.setTextViewText(R.id.username,username);
            views.setOnClickPendingIntent(R.id.name, pendingIntent);
            views.setOnClickPendingIntent(R.id.username, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.name);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.username);


        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

