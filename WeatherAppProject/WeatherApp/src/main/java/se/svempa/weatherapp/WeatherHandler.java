package se.svempa.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by rle on 9/12/13.
 */
// Implementation of AsyncTask used to download XML from www.yr.no.
public class WeatherHandler extends AsyncTask<String, Void, WeatherReport> {
    private static final String URL = "http://www.yr.no/place/";
    private Context context;
    private WeatherActivity callback;
    private WeatherAppWidgetService widgetCallback;
    private String country;
    private String region;
    private String city;

    public WeatherHandler(Context context, WeatherActivity callback, String country, String region, String city) {
        this.context = context;
        this.callback = callback;
        this.country = country;
        this.region = region;
        this.city = city;
    }

    public WeatherHandler(Context context, WeatherAppWidgetService widgetCallback, String country, String region, String city) {
        this.context = context;
        this.widgetCallback = widgetCallback;
        this.country = country;
        this.region = region;
        this.city = city;
    }

    // Uses AsyncTask to download the XML.
    public void update() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(callback != null){
            // if we are calling from activity
            // check if network is available, otherwise error
            if (networkInfo != null && networkInfo.isConnected()) {
                if(country == "" && region == "" && city == ""){
                    // if values are not set, fetch weather for Växjö
                    execute(URL, "Sweden", "Kronoberg", "Växjö");
                } else {
                    execute(URL, country, region, city);
                }
                callback.showProgressDialog();
            } else {
                callback.showNetworkDialog();
            }
        } else {
            // if we are calling from the widget service
            // check if network is available, otherwise error
            if (networkInfo != null && networkInfo.isConnected()) {
                if(country == "" && region == "" && city == ""){
                    // if values are not set, fetch weather for Växjö
                    execute(URL, "Sweden", "Kronoberg", "Växjö");
                } else {
                    execute(URL, country, region, city);
                }
                widgetCallback.progressFeedback();
            } else {
                widgetCallback.errorFeedback();
            }
        }

    }


    @Override
    protected WeatherReport doInBackground(String... urls) {
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            String error = context.getResources().getString(R.string.connection_error);
            Log.e(WeatherHandler.class.getName(), error, e);
            return new WeatherReport(error);
        } catch (XmlPullParserException e) {
            String error = context.getResources().getString(R.string.xml_error);
            Log.e(WeatherHandler.class.getName(), error, e);
            return new WeatherReport(error);
        }
    }

    @Override
    protected void onPostExecute(WeatherReport result) {
        if(callback != null){
            // once the weather report is parsed and packaged, send to main activity for display
            callback.updateWeatherReport(result);
            callback.hideProgressDialog();
        } else {
            // if we are calling from widget
            widgetCallback.updateWidgetWeather(result);
            widgetCallback.completeFeedback();
        }

    }

    // Create a WeatherReport for a specific city in a certain region in a country
    private WeatherReport loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        urlString = urlString + country + "/" + region + "/" + city + "/forecast.xml";
        InputStream stream = null;
        // Instantiate the parser
        WeatherParser weatherXmlParser = new WeatherParser();
        List<WeatherForecast> entries = null;

        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        WeatherReport report = new WeatherReport();
        report.setStatus("OK");
        report.setCity(city);
        report.setCountry(country);
        report.setRegion(region);

        report.setCreationDate(formatter.format(rightNow.getTime()));

        try {
            stream = downloadUrl(urlString);
            entries = weatherXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // WeatherParser returns a List (called "entries") of WeatherForecast objects.
        // Each WeatherForecast object represents a forecast in the XML.
        // This section processes the forecast list and adds the forecasts to the report.
        for (WeatherForecast forecast : entries) {
            report.addForecast(forecast);
        }
        return report;
    }

    // Given a string representation of a URL, sets up a connection and gets an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
