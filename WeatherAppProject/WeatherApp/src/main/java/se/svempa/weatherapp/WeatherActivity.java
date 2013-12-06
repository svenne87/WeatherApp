package se.svempa.weatherapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import se.svempa.weatherapp.WeatherArrayAdapter;

import java.util.ArrayList;

public class WeatherActivity extends ListActivity {
    private WeatherHandler weatherHandler;
    private WeatherArrayAdapter adapter;
    private ArrayList<WeatherForecast> list = new ArrayList<WeatherForecast>();
    private ProgressDialog progressDialog = null;
    private TextView cityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = (TextView) findViewById(R.id.city_text);

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

        adapter = new WeatherArrayAdapter(this, list);
        setListAdapter(adapter);
    }


    public void updateWeatherReport(WeatherReport result) {
        list.clear();
        list.addAll(result.getForecast());
        adapter.notifyDataSetChanged();
        cityText.setText(result.getCity());
    }


    // show loading dialog
    public void showProgressDialog(){
        if(progressDialog ==  null){
            // display dialog when loading data
            progressDialog = ProgressDialog.show(this, "Loading weather data!", "Please Wait...", true, false);
        }
    }

    // hide loading dialog
    public void hideProgressDialog(){
        // if loading dialog is visible, then hide it
        if(progressDialog != null){
            progressDialog.cancel();
        }
    }

    // show retry/cancel for network dialog
    public void showNetworkDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Network not found!");
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    weatherHandler.update();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    // go to home screen
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
    }

}
