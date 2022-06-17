package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Entities.WeatherCache;
import Models.Current;
import Models.Weather;

public class WeatherDAO {

    private DataContext dataContext;
    private SQLiteDatabase banco;

    private String[] parameters = new String[] {
            "Id",
            "Name",
            "Region",
            "Country",
            "Latitude",
            "Longitude",
            "Localtime",
            "TempC",
            "TempF",
            "WindMph",
            "WindKph",
            "PrecipMm",
            "PrecipIn"
    };

    public WeatherDAO(Context  context){
        dataContext = new DataContext(context);
        banco = dataContext.getWritableDatabase();
    }

    public long Create(Weather weather){
        ContentValues values = new ContentValues();

        values.put("Name", weather.location.name);
        values.put("Region", weather.location.region);
        values.put("Country", weather.location.country);
        values.put("Latitude", weather.location.lat);
        values.put("Longitude", weather.location.lon);
        values.put("Localtime", weather.location.localtime);
        values.put("TempC", weather.current.temp_c);
        values.put("TempF", weather.current.temp_f);
        values.put("WindMph", weather.current.wind_mph);
        values.put("WindKph", weather.current.wind_kph);
        values.put("PrecipMm", weather.current.precip_mm);
        values.put("PrecipIn", weather.current.precip_in);

        return banco.insert("WeatherCache", null, values);
    }

    public ArrayList<WeatherCache> Read(String cidade){
        ArrayList<WeatherCache> weatherCaches =  new ArrayList<>();

        Cursor cursor = banco.query("WeatherCache",  parameters, "Name=?", new String[] { cidade }, null, null, null);

        while(cursor.moveToNext()){
            WeatherCache c = getWeatherCacheWithCursor(cursor);

            weatherCaches.add(c);
        }

        return weatherCaches;
    }

    public WeatherCache GetById(int id){
        Cursor cursor = banco.query("WeatherCache",  parameters, "Id=?", new String[] { String.valueOf(id) }, null, null, null);

        while(cursor.moveToNext()){
            WeatherCache c = getWeatherCacheWithCursor(cursor);

            return c;
        }

        return null;
    }

    @NonNull
    private WeatherCache getWeatherCacheWithCursor(Cursor cursor) {
        WeatherCache c = new WeatherCache();

        c.Id = cursor.getInt(0);
        c.Name = cursor.getString(1);
        c.Region = cursor.getString(2);
        c.Country = cursor.getString(3);
        c.Latitude = cursor.getDouble(4);
        c.Longitude = cursor.getDouble(5);
        c.Localtime = cursor.getString(6);
        c.TempC = cursor.getDouble(7);
        c.TempF = cursor.getDouble(8);
        c.WindMph = cursor.getDouble(9);
        c.WindKph = cursor.getDouble(10);
        c.PrecipMm = cursor.getDouble(11);
        c.PrecipIn = cursor.getDouble(12);
        return c;
    }

    public int getActiveWeather() {
        Cursor cursor = banco.query("ActiveWeather",
                new String[] {
                        "WeatherId"
                }, null, null, null, null, null);

        while(cursor.moveToNext()){
            return cursor.getInt(0);
        }

        return -1;
    }

    public void SetActive(int Id) {
        banco.delete("ActiveWeather", "1=1", new String[] {});

        ContentValues values = new ContentValues();

        values.put("WeatherId", Id);

        banco.insert("ActiveWeather", null, values);
    }

    public void Delete(WeatherCache weather){
        banco.delete("WeatherCache", "Name=?", new String[]{ String.valueOf(weather.Name) }  );
    }
}
