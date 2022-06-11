package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Entities.WeatherCache;
import Models.Current;
import Models.Weather;

public class WeatherDAO {

    private DataContext dataContext;
    private SQLiteDatabase banco;

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

        return banco.insert("Current", null, values);
    }

    public List<WeatherCache> Read(String cidade){
        List<WeatherCache> weatherCaches =  new ArrayList<>();

        Cursor cursor = banco.query("Current",
            new String[] {
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
            }, "Name=?", new String[] { cidade }, null, null, null);

        while(cursor.moveToNext()){
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

            weatherCaches.add(c);
        }

        return weatherCaches;
    }

    public void Delete(WeatherCache weather){
        banco.delete("WeatherCache", "Name=?", new String[]{ String.valueOf(weather.Name) }  );
    }


}
