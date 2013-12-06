package se.svempa.weatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class WeatherAppWidgetProvider extends AppWidgetProvider {

    private static final String SHOW_SETTINGS_POPUP_ACTION = "se.svempa.weatherapp.weatherwidgetsettings";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // used to launch settings activity
        if(intent.getAction().equals(SHOW_SETTINGS_POPUP_ACTION)){
            Intent popUpIntent = new Intent(context, WeatherWidgetSettings.class);
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            popUpIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            popUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(popUpIntent);
        }

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Perform this loop procedure for each App Widget
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            // intent to call service
            Intent intent = new Intent(context, WeatherAppWidgetService.class);
            intent.setAction("widgetupdate");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // was 0 instead of appWidgetId before this caused to update_button to stop working
            // because the service got id 13 instead of 12.
            // sending appWidgetId instead of 0 solved this
            PendingIntent pendingIntent = PendingIntent.getService(context, appWidgetId, intent, 0);

            views.setOnClickPendingIntent(R.id.update_button, pendingIntent);


            // intent to call weather activity
            Intent activityIntent = new Intent(context, WeatherActivity.class);
            activityIntent.setAction("launchweatheractivity");
            PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

            views.setOnClickPendingIntent(R.id.widget_layout, activityPendingIntent);


            Intent settingsIntent = new Intent(context, WeatherAppWidgetProvider.class);
            settingsIntent.setAction(SHOW_SETTINGS_POPUP_ACTION);
            settingsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent settingsPendingIntent = PendingIntent.getBroadcast(context, 0, settingsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.settings_button, settingsPendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }


}
