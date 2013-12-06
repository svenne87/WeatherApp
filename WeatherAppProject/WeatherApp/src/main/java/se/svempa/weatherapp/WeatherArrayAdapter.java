package se.svempa.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Svempa on 2013-10-28.
 */
public class WeatherArrayAdapter extends ArrayAdapter<WeatherForecast>{

    private final Context context;
    private final ArrayList<WeatherForecast> weatherForecasts;

    public WeatherArrayAdapter(Context context, ArrayList<WeatherForecast> weatherForecasts){
        super(context, R.layout.weather_list, weatherForecasts);
        this.context = context;
        this.weatherForecasts = weatherForecasts;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.weather_list, parent, false);
        TextView temperatureTextView = (TextView) rowView.findViewById(R.id.temperature);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
        TextView windTextView = (TextView) rowView.findViewById(R.id.wind);
        TextView precipitationTextView = (TextView) rowView.findViewById(R.id.precipitation);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.weather_image);

        // get forecast
        WeatherForecast weatherForecast = weatherForecasts.get(position);

        // set forecast texts
        temperatureTextView.setText(weatherForecast.temperature.value + "° C");
        dateTextView.setText(weatherForecast.dateString());
        windTextView.setText(weatherForecast.windDirection.name + " " + weatherForecast.windSpeed.mps + " m/s");
        precipitationTextView.setText(weatherForecast.precipitation.value + " mm");

        String weatherSymbol = weatherForecast.symbol.number + " " + weatherForecast.symbol.var;

        // set correct weather image
        if(weatherSymbol.indexOf("1") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_fullmoon);
        } else if (weatherSymbol.indexOf("1") != -1){
            imageView.setImageResource(R.drawable.weezle_sun);
        } else if (weatherSymbol.indexOf("2") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_moon_cloud);
        } else if (weatherSymbol.indexOf("2") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_minimal_clouds);
        } else if (weatherSymbol.indexOf("3") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_moon_cloud_medium);
        } else if (weatherSymbol.indexOf("3") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_maximum_clouds);
        } else if (weatherSymbol.indexOf("4") != -1){
            imageView.setImageResource(R.drawable.weezle_max_cloud);
        } else if (weatherSymbol.indexOf("5") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_night_rain);
        } else if (weatherSymbol.indexOf("5") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_medium_rain);
        } else if (weatherSymbol.indexOf("6") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_night_thunder_rain);
        } else if (weatherSymbol.indexOf("6") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_thunder_rain);
        } else if (weatherSymbol.indexOf("7") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_night_flurry);
        } else if (weatherSymbol.indexOf("7") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_flurrie);
        } else if (weatherSymbol.indexOf("8") != -1 && weatherSymbol.indexOf("n") != -1){
            imageView.setImageResource(R.drawable.weezle_snow);
        } else if (weatherSymbol.indexOf("8") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_and_snow);
        } else if (weatherSymbol.indexOf("9") != -1){
            imageView.setImageResource(R.drawable.weezle_medium_rain);
        } else if (weatherSymbol.indexOf("10") != -1){
            imageView.setImageResource(R.drawable.weezle_rain);
        } else if (weatherSymbol.indexOf("11") != -1){
            imageView.setImageResource(R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("12") != -1){
            imageView.setImageResource(R.drawable.weezle_medium_ice);
        } else if (weatherSymbol.indexOf("13") != -1){
            imageView.setImageResource(R.drawable.weezle_snow);
        } else if (weatherSymbol.indexOf("14") != -1){
            imageView.setImageResource(R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("15") != -1){
            imageView.setImageResource(R.drawable.weezle_fog);
        } else if (weatherSymbol.indexOf("20") != -1){
            imageView.setImageResource(R.drawable.weezle_sun_thunder_rain);
        } else if (weatherSymbol.indexOf("21") != -1){
            imageView.setImageResource(R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("22") != -1){
            imageView.setImageResource(R.drawable.weezle_cloud_thunder_rain);
        } else if (weatherSymbol.indexOf("23") != -1){
            imageView.setImageResource(R.drawable.weezle_cloud_thunder_rain);
        }


        return rowView;
    }

}
