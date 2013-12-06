package se.svempa.weatherapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rle on 9/12/13.
 */
// parses XML from www.yr.no
public class WeatherParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readForecast(parser);
        } finally {
            in.close();
        }
    }

    private List readForecast(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "weatherdata");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("forecast")) {
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    name = parser.getName();
                    if (name.equals("tabular")) {
                        while (parser.next() != XmlPullParser.END_TAG) {
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            name = parser.getName();
                            if (name.equals("time")) {
                                entries.add(readEntry(parser));
                            } else {
                                skip(parser);
                            }
                        }
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private WeatherForecast readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        WeatherForecast forecast = new WeatherForecast();
        forecast.from = parser.getAttributeValue(ns, "from");
        forecast.to = parser.getAttributeValue(ns, "to");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("symbol")) {
                readSymbol(parser, forecast);
            } else if (name.equals("precipitation")) {
                readPrecipitation(parser, forecast);
            } else if (name.equals("windDirection")) {
                readWindDirection(parser, forecast);
            } else if (name.equals("windSpeed")) {
                readWindSpeed(parser, forecast);
            } else if (name.equals("temperature")) {
                readTemperature(parser, forecast);
            } else if (name.equals("pressure")) {
                readPressure(parser, forecast);
            } else {
                skip(parser);
            }
        }
        return forecast;
    }

    private void readSymbol(XmlPullParser parser, WeatherForecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "symbol");
        forecast.symbol.name = parser.getAttributeValue(ns, "name");
        forecast.symbol.number = parser.getAttributeValue(ns, "number");
        forecast.symbol.var = parser.getAttributeValue(ns, "var");
        parser.next();
        parser.require(XmlPullParser.END_TAG, ns, "symbol");
    }

    private void readPrecipitation(XmlPullParser parser, WeatherForecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "precipitation");
        forecast.precipitation.value = parser.getAttributeValue(ns, "value");
        forecast.precipitation.minvalue = parser.getAttributeValue(ns, "minvalue");
        forecast.precipitation.maxvalue = parser.getAttributeValue(ns, "maxvalue");
        parser.next();
        parser.require(XmlPullParser.END_TAG, ns, "precipitation");
    }

    private void readWindDirection(XmlPullParser parser, WeatherForecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "windDirection");
        forecast.windDirection.name = parser.getAttributeValue(ns, "name");
        forecast.windDirection.deg = parser.getAttributeValue(ns, "deg");
        forecast.windDirection.code = parser.getAttributeValue(ns, "code");
        parser.next();
        parser.require(XmlPullParser.END_TAG, ns, "windDirection");
    }

    private void readWindSpeed(XmlPullParser parser, WeatherForecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "windSpeed");
        forecast.windSpeed.name = parser.getAttributeValue(ns, "name");
        forecast.windSpeed.mps = parser.getAttributeValue(ns, "mps");
        parser.next();
        parser.require(XmlPullParser.END_TAG, ns, "windSpeed");
    }

    private void readTemperature(XmlPullParser parser, WeatherForecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "temperature");
        forecast.temperature.value = parser.getAttributeValue(ns, "value");
        forecast.temperature.unit = parser.getAttributeValue(ns, "unit");
        parser.next();
        parser.require(XmlPullParser.END_TAG, ns, "temperature");
    }

    private void readPressure(XmlPullParser parser, WeatherForecast forecast) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pressure");
        forecast.pressure.value = parser.getAttributeValue(ns, "value");
        forecast.pressure.unit = parser.getAttributeValue(ns, "unit");
        parser.next();
        parser.require(XmlPullParser.END_TAG, ns, "pressure");
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
