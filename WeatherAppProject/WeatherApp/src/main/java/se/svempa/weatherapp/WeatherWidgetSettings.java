package se.svempa.weatherapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class WeatherWidgetSettings extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener{

    private Button closeButton;
    private Button saveButton;
    private Spinner regionSpinner;
    private Spinner citySpinner;
    private ArrayAdapter cityAdapter;
    private ArrayAdapter regionAdapter;

    private List regionList;
    private List cityList;
    private String selectedRegion;
    private String selectedCity;
    private int selectedRegionPos;
    private int selectedCityPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_widget_settings_layout);

        closeButton = (Button) findViewById(R.id.close_button);
        saveButton = (Button) findViewById(R.id.save_button);
        regionSpinner = (Spinner) findViewById(R.id.region_spinner);
        citySpinner = (Spinner) findViewById(R.id.city_spinner);

        closeButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        regionList = new ArrayList();
        cityList = new ArrayList();

        // adding all regions to regionList
        addRegionsToList();

        regionAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, regionList);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
        regionSpinner.setOnItemSelectedListener(this);

        cityAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == closeButton){
            // close activity
            WeatherWidgetSettings.this.finish();
        } else if (v == saveButton) {
            // save the selected Region and City to SharedPreferences
            if(selectedRegion != null && selectedCity != null){
                SharedPreferences prefs = this.getSharedPreferences("se.svempa.weatherapp", Context.MODE_PRIVATE);
                prefs.edit().putString("WeatherRegion", selectedRegion).commit();
                prefs.edit().putString("WeatherCity", selectedCity).commit();

                // start the WeatherAppWidgetService to update the widget with the new region and city
                Intent updateWidgetIntent = new Intent(this, WeatherAppWidgetService.class);
                updateWidgetIntent.setAction("widgetupdatefromservice");

                // get int sent here with intent
                int widgetId = this.getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

                // set int to intent we are about to sent
                updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                this.startService(updateWidgetIntent);
            }

            // close activity
            WeatherWidgetSettings.this.finish();
        }

    }

    // adding all regions to list
    private void addRegionsToList(){
        regionList.add("Blekinge");
        regionList.add("Dalarna");
        regionList.add("Gävleborg");
        regionList.add("Gotland");
        regionList.add("Halland");
        regionList.add("Jämtland");
        regionList.add("Jönköping");
        regionList.add("Kalmar");
        regionList.add("Kronoberg");
        regionList.add("Norrbotten");
        regionList.add("Örebro");
        regionList.add("Östergötland");
        regionList.add("Södermanland");
        regionList.add("Stockholm");
        regionList.add("Uppsala");
        regionList.add("Värmland");
        regionList.add("Västerbotten");
        regionList.add("Västernorrland");
        regionList.add("Västmanland");
        regionList.add("Västra Götaland");
        regionList.add("Skåne");
    }


    // adding cities for region "Blekinge"
    private void addCitiesToListZero(){
        cityList.add("Karlshamn");
        cityList.add("Karlskrona");
        cityList.add("Olofström");
        cityList.add("Ronneby");
        cityList.add("Sölvesborg");
    }

    // adding cities for region "Dalarna"
    private void addCitiesToListOne(){
        cityList.add("Avesta");
        cityList.add("Borlänge");
        cityList.add("Falun");
        cityList.add("Ludvika");
        cityList.add("Mora");
    }

    // adding cities for region "Gävleborg"
    private void addCitiesToListTwo(){
        cityList.add("Bollnäs");
        cityList.add("Gävle");
        cityList.add("Hudiksvall");
        cityList.add("Sandviken");
        cityList.add("Söderhamn");
    }

    // adding cities for region "Gotland"
    private void addCitiesToListThree(){
        cityList.add("Fårösund");
        cityList.add("Hemse");
        cityList.add("Klintehamn");
        cityList.add("Slite");
        cityList.add("Visby");
    }

    // adding cities for region "Halland"
    private void addCitiesToListFour(){
        cityList.add("Falkenberg");
        cityList.add("Halmstad");
        cityList.add("Kungsbacka");
        cityList.add("Laholm");
        cityList.add("Varberg");
    }

    // adding cities for region "Jämtland"
    private void addCitiesToListFive(){
        cityList.add("Brunflo");
        cityList.add("Krokom");
        cityList.add("Östersund");
        cityList.add("Strömsund");
        cityList.add("Sveg");
    }

    // adding cities for region "Jönköping"
    private void addCitiesToListSix(){
        cityList.add("Huskvarna");
        cityList.add("Jönköping");
        cityList.add("Nässjö");
        cityList.add("Nässjö");
        cityList.add("Tranås");
        cityList.add("Värnamo");
        cityList.add("Vetlanda");
    }

    // adding cities for region "Kalmar"
    private void addCitiesToListSeven(){
        cityList.add("Kalmar");
        cityList.add("Nybro");
        cityList.add("Oskarshamn");
        cityList.add("Västervik");
        cityList.add("Vimmerby");
    }

    // adding cities for region "Kronoberg"
    private void addCitiesToListEight(){
        cityList.add("Älmhult");
        cityList.add("Alvesta");
        cityList.add("Lindshammar");
        cityList.add("Ljungby");
        cityList.add("Markaryd");
        cityList.add("Växjö");
    }

    // adding cities for region "Norrbotten"
    private void addCitiesToListNine(){
        cityList.add("Boden");
        cityList.add("Gällivare");
        cityList.add("Kiruna");
        cityList.add("Luleå");
        cityList.add("Piteå");
    }

    // adding cities for region "Örebro"
    private void addCitiesToListTen(){
        cityList.add("Degerfors");
        cityList.add("Karlskoga");
        cityList.add("Kumla");
        cityList.add("Lindesberg");
        cityList.add("Örebro");
    }

    // adding cities for region "Östergötland"
    private void addCitiesToListEleven(){
        cityList.add("Finspång");
        cityList.add("Linköping");
        cityList.add("Mjölby");
        cityList.add("Motala");
        cityList.add("Norrköping");
    }

    // adding cities for region "Södermanland"
    private void addCitiesToListTwelve(){
        cityList.add("Eskilstuna");
        cityList.add("Katrineholm");
        cityList.add("Nyköping");
        cityList.add("Oxelösund");
        cityList.add("Strängnäs");
    }

    // adding cities for region "Stockholm"
    private void addCitiesToListThirteen(){
        cityList.add("Åkersberga");
        cityList.add("Årsta");
        cityList.add("Boo");
        cityList.add("Bromma");
        cityList.add("Haninge");
        cityList.add("Huddinge");
        cityList.add("Jakobsberg");
        cityList.add("Kista");
        cityList.add("Lidingö");
        cityList.add("Märsta");
        cityList.add("Nacka");
        cityList.add("Norrtälje");
        cityList.add("Nynäshamn");
        cityList.add("Östermalm");
        cityList.add("Södertälje");
        cityList.add("Sollentuna");
        cityList.add("Solna");
        cityList.add("Stockholm");
        cityList.add("Sundbyberg");
        cityList.add("Täby");
        cityList.add("Tullinge");
        cityList.add("Tumba");
        cityList.add("Upplands Väsby");
        cityList.add("Vallentuna");
        cityList.add("Västerhaninge");
    }

    // adding cities for region "Uppsala"
    private void addCitiesToListFourteen(){
        cityList.add("Älvkarleby");
        cityList.add("Bålsta");
        cityList.add("Enköping");
        cityList.add("Östhammar");
        cityList.add("Rörsby");
        cityList.add("Uppsala");
    }

    // adding cities for region "Värmland"
    private void addCitiesToListFifteen(){
        cityList.add("Arvika");
        cityList.add("Karlstad");
        cityList.add("Kristinehamn");
        cityList.add("Säffle");
        cityList.add("Skoghall");
    }

    // adding cities for region "Västerbotten"
    private void addCitiesToListSixteen(){
        cityList.add("Holmsund");
        cityList.add("Lycksele");
        cityList.add("Skellefteå");
        cityList.add("Stavasjö");
        cityList.add("Umeå");
        cityList.add("Vännäs");
    }

    // adding cities for region "Västernorrland"
    private void addCitiesToListSeventeen(){
        cityList.add("Härnösand");
        cityList.add("Örnsköldsvik");
        cityList.add("Sollefteå");
        cityList.add("Sundsvall");
        cityList.add("Timrå");
    }

    // adding cities for region "Västmanland"
    private void addCitiesToListEighteen(){
        cityList.add("Arboga");
        cityList.add("Fagersta");
        cityList.add("Köping");
        cityList.add("Sala");
        cityList.add("Västerås");
    }

    // adding cities for region "Västra Götaland"
    private void addCitiesToListNineteen(){
        cityList.add("Alingsås");
        cityList.add("Borås");
        cityList.add("Falköping");
        cityList.add("Gothenburg");
        cityList.add("Kinna");
        cityList.add("Kungälv");
        cityList.add("Lerum");
        cityList.add("Lidköping");
        cityList.add("Majorna");
        cityList.add("Mariestad");
        cityList.add("Mölndal");
        cityList.add("Mölnlycke");
        cityList.add("Partille");
        cityList.add("Skövde");
        cityList.add("Trollhättan");
        cityList.add("Uddevalla");
        cityList.add("Vänersborg");
    }

    // add cities for region "Skåne"
    private void addCitiesToListTwenty(){
        cityList.add("Ängelholm");
        cityList.add("Eslöv");
        cityList.add("Hässleholm");
        cityList.add("Helsingborg");
        cityList.add("Höganäs");
        cityList.add("Kävlinge");
        cityList.add("Kristianstad");
        cityList.add("Landskrona");
        cityList.add("Lund");
        cityList.add("Malmö");
        cityList.add("Staffanstorp");
        cityList.add("Trelleborg");
        cityList.add("Valje");
        cityList.add("Ystad");
    }

    // find out what region was selected (by item position in spinner list) and add correct cities
    // if there is a stored city in SharedPreferences from then try to set it as selected item
    private void addCorrectCitiesByRegion(int selectedValue){

        if(selectedValue == 0){
            addCitiesToListZero();
        } else if(selectedValue == 1){
            addCitiesToListOne();
        } else if(selectedValue == 2){
            addCitiesToListTwo();
        } else if(selectedValue == 3){
            addCitiesToListThree();
        } else if(selectedValue == 4){
            addCitiesToListFour();
        } else if(selectedValue == 5){
            addCitiesToListFive();
        } else if(selectedValue == 6){
            addCitiesToListSix();
        } else if(selectedValue == 7){
            addCitiesToListSeven();
        } else if(selectedValue == 8){
            addCitiesToListEight();
        } else if(selectedValue == 9){
            addCitiesToListNine();
        } else if(selectedValue == 10){
            addCitiesToListTen();
        } else if(selectedValue == 11){
            addCitiesToListEleven();
        } else if(selectedValue == 12){
            addCitiesToListTwelve();
        } else if(selectedValue == 13){
            addCitiesToListThirteen();
        } else if(selectedValue == 14){
            addCitiesToListFourteen();
        } else if(selectedValue == 15){
            addCitiesToListFifteen();
        } else if(selectedValue == 16){
            addCitiesToListSixteen();
        } else if(selectedValue == 17){
            addCitiesToListSeventeen();
        } else if(selectedValue == 18){
            addCitiesToListEighteen();
        } else if(selectedValue == 19){
            addCitiesToListNineteen();
        } else if(selectedValue == 20){
            addCitiesToListTwenty();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selectedRegionPos = regionSpinner.getSelectedItemPosition();

        // clear all cities in cityList
        cityList.clear();

        // add the correct new cities to cityList and notify adapter about change
        addCorrectCitiesByRegion(selectedRegionPos);
        cityAdapter.notifyDataSetChanged();

        selectedRegion = String.valueOf(regionSpinner.getSelectedItem());
        selectedCity = String.valueOf(citySpinner.getSelectedItem());
        selectedCityPos = citySpinner.getSelectedItemPosition();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}