package se.svempa.weatherapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rle on 9/12/13.
 */
// Weather report with some meta information and the list of forecasts
public class WeatherReport {
    private String status;
    private List<WeatherForecast> forecast = new ArrayList<WeatherForecast>();
    private String city;
    private String country;
    private String region;
    private String creationDate;

    public WeatherReport() {

    }

    public WeatherReport(String status) {
        this.status = status;
    }

    public List<WeatherForecast> getForecast() {
        return forecast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion(){
        return region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void addForecast(WeatherForecast forecast) {
        this.forecast.add(forecast);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Location: " + city + ", " + country + "\n");
        sb.append("Updated: " + creationDate);
        for (WeatherForecast f : forecast) {
            sb.append(f.toString() + "\n");
        }
        return sb.toString();
    }
}
