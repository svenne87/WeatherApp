package se.svempa.weatherapp;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.security.Provider;

/**
 * Created by Svempa on 2013-11-05.
 */
public class WeatherAppWidgetService  extends Service {

    private RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widget);
    private WeatherHandler weatherHandler;
    private AppWidgetManager appWidgetMan;
    private WeatherForecast weatherForecast;
    private int widgetId;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        updateWidget(intent);
        stopSelf(startId);

        return START_STICKY;
    }


    public void updateWidgetWeather(WeatherReport result){

        // set forecast
        weatherForecast = result.getForecast().get(0);

        views.setTextViewText(R.id.temperature, weatherForecast.temperature.value + "° C");
        views.setTextViewText(R.id.date, weatherForecast.widgetDateString());
        views.setTextViewText(R.id.wind, weatherForecast.windDirection.name + " " + weatherForecast.windSpeed.mps + " m/s");
        views.setTextViewText(R.id.precipitation, weatherForecast.precipitation.value + " mm");
        views.setTextViewText(R.id.city, result.getCity());

        String weatherSymbol = weatherForecast.symbol.number + " " + weatherForecast.symbol.var;

        // set correct weather image
        if(weatherSymbol.indexOf("1") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_fullmoon);
        } else if (weatherSymbol.indexOf("1") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun);
        } else if (weatherSymbol.indexOf("2") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_moon_cloud);
        } else if (weatherSymbol.indexOf("2") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_minimal_clouds);
        } else if (weatherSymbol.indexOf("3") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_moon_cloud_medium);
        } else if (weatherSymbol.indexOf("3") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_maximum_clouds);
        } else if (weatherSymbol.indexOf("4") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_max_cloud);
        } else if (weatherSymbol.indexOf("5") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_night_rain);
        } else if (weatherSymbol.indexOf("5") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_medium_rain);
        } else if (weatherSymbol.indexOf("6") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_night_thunder_rain);
        } else if (weatherSymbol.indexOf("6") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_thunder_rain);
        } else if (weatherSymbol.indexOf("7") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_night_flurry);
        } else if (weatherSymbol.indexOf("7") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_flurrie);
        } else if (weatherSymbol.indexOf("8") != -1 && weatherSymbol.indexOf("n") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_snow);
        } else if (weatherSymbol.indexOf("8") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_and_snow);
        } else if (weatherSymbol.indexOf("9") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_medium_rain);
        } else if (weatherSymbol.indexOf("10") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_rain);
        } else if (weatherSymbol.indexOf("11") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("12") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_medium_ice);
        } else if (weatherSymbol.indexOf("13") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_snow);
        } else if (weatherSymbol.indexOf("14") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("15") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_fog);
        } else if (weatherSymbol.indexOf("20") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_sun_thunder_rain);
        } else if (weatherSymbol.indexOf("21") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("22") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("23") != -1){
            views.setImageViewResource(R.id.weather_image, R.drawable.weezle_cloud_thunder_rain);
        }
    }


    // when report is getting fetched
    public void progressFeedback(){
        views.setTextViewText(R.id.update_text, "Loading...");
        appWidgetMan.updateAppWidget(widgetId, views);
    }

    // if a network error occurs
    public void errorFeedback(){
        views.setTextViewText(R.id.update_text, "Error occured!");
        appWidgetMan.updateAppWidget(widgetId, views);
    }

    // when the report is done loading
    public void completeFeedback(){
        //set texts to views
        views.setTextViewText(R.id.update_text, "");
        appWidgetMan.updateAppWidget(widgetId, views);
    }

    private void updateWidget(Intent intent){

        if (intent != null){
            String requestedAction = intent.getAction();
            if (requestedAction != null && requestedAction.equals("widgetupdate") || requestedAction != null && requestedAction.equals("widgetupdatefromservice")){

                widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
                appWidgetMan = AppWidgetManager.getInstance(this);

                String region = null;
                String city = null;

                // try fetching region and city from SharedPreferences
                SharedPreferences prefs = this.getSharedPreferences("se.svempa.weatherapp", Context.MODE_PRIVATE);
                try{
                    region = prefs.getString("WeatherRegion", "");
                    city = prefs.getString("WeatherCity", "");
                } catch(NullPointerException ex){
                    Log.e("SharedPrefs Exception", "ServiceClass");
                }

                // get weather report for region and city set in SharedPreferences
                if(region != null && region != "" && city != null && city != ""){
                    weatherHandler = new WeatherHandler(getApplicationContext(), this, "Sweden", region, city);
                    weatherHandler.update();
                } else {
                    // if not set, get the weather report for Växjö
                    weatherHandler = new WeatherHandler(getApplicationContext(), this, "Sweden", "Kronoberg", "Växjö");
                    weatherHandler.update();
                }

                Log.i("WidgetID", Integer.toString(widgetId));

            }
        }
    }

}
