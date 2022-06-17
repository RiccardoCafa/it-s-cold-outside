package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataContext extends SQLiteOpenHelper {

    private static final String name="banco.db";
    private static final int version=1;

    public DataContext(Context context ) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS WeatherCache (" +
                "Id INTEGER NOT NULL PRIMARY KEY, " +
                "Name VARCHAR(8) NOT NULL, " +
                "Region VARCHAR(5) NOT NULL, " +
                "Country VARCHAR(6) NOT NULL, " +
                "Latitude NUMERIC(6,2) NOT NULL, " +
                "Longitude NUMERIC(6,2) NOT NULL, " +
                "Localtime VARCHAR(16) NOT NULL, " +
                "TempC INTEGER  NOT NULL, " +
                "TempF NUMERIC(4,1) NOT NULL, " +
                "WindMph NUMERIC(3,1) NOT NULL, " +
                "WindKph INTEGER  NOT NULL, " +
                "PrecipMm NUMERIC(3,1) NOT NULL, " +
                "PrecipIn BIT  NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS ActiveWeather(WeatherId INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

