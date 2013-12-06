package se.svempa.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rle on 9/12/13.
 * <p/>
 * Matches following XML fragment
 * <time from="2013-09-13T18:00:00" to="2013-09-14T00:00:00" period="3">
 * <!-- Valid from 2013-09-13T18:00:00 to 2013-09-14T00:00:00 -->
 * <symbol number="3" name="Partly cloudy" var="mf/03n.27"/>
 * <precipitation value="0" minvalue="0" maxvalue="0.3"/>
 * <!-- Valid at 2013-09-13T18:00:00 -->
 * <windDirection deg="60.8" code="ENE" name="East-northeast"/>
 * <windSpeed mps="1.0" name="Light air"/>
 * <temperature unit="celsius" value="18"/>
 * <pressure unit="hPa" value="1014.3"/>
 * </time>
 */
public class WeatherForecast {
    public String from;
    public String to;
    public Symbol symbol = new Symbol();
    public Precipitation precipitation = new Precipitation();
    public WindDirection windDirection = new WindDirection();
    public WindSpeed windSpeed = new WindSpeed();
    public Temperature temperature = new Temperature();
    public Pressure pressure = new Pressure();

    private static String getDate(String date) {
        return date.substring(0, 10);
    }

    private static String getTime(String time) {
        return time.substring(11, 16);
    }

    @Override
    public String toString() {
        if (from == null) {
            return "Test";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("WeatherForecast, " + getDate(from) + "\n");
        sb.append(getTime(from) + " - " + getTime(to) + "\n");
        sb.append(symbol.name + ", " + precipitation.value + " mm, " + windDirection.name + ", " + windSpeed.mps +
                " m/s, " + windSpeed.name + ", " + temperature.value + " " + temperature.unit +
                ", " + pressure.value + " " + pressure.unit + "\n");
        return sb.toString();
    }

    public String dateString(){
        String date = getDate(from);
        String timeFrom = getTime(from);
        String timeTo = getTime(to);

        return date + " " + timeFrom + " - " + timeTo;
    }

    // return date + current time
    public String widgetDateString(){
        String date = getDate(from);
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return date + " " + sdf.format(cal.getTime());
    }


    public static class Symbol {
        public String name;
        public String number;
        public String var;
    }

    public static class Precipitation {
        public String value;
        public String minvalue;
        public String maxvalue;
    }

    public static class WindDirection {
        public String deg;
        public String code;
        public String name;
    }

    public static class WindSpeed {
        public String mps;
        public String name;
    }

    public static class Temperature {
        public String unit;
        public String value;
    }

    public static class Pressure {
        public String unit;
        public String value;
    }
}