package Controllers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import Models.Current;
import Models.Location;
import Models.Weather;

public class WeatherController {

    private static final String URL = "https://api.weatherapi.com/v1/current.json?key=eb3709bc43824ed3bc3205224221006";

    public static final Weather getAsObject(String cidade) throws WeatherException, JSONException {

        String jsonResult = WeatherController.get(cidade);

        JSONObject obj = null;

        try {
            obj = new JSONObject(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!obj.has("erro")) {
            Weather weatherObj = new Weather();

            JSONObject locationJson = obj.getJSONObject("location");
            JSONObject currentJson = obj.getJSONObject("current");

            weatherObj.location = new Location();

            weatherObj.location.name = locationJson.getString("name");
            weatherObj.location.region = locationJson.getString("region");
            weatherObj.location.country = locationJson.getString("country");
            weatherObj.location.lat = locationJson.getDouble("lat");
            weatherObj.location.lon = locationJson.getDouble("lon");
            weatherObj.location.localtime = locationJson.getString("localtime");

            weatherObj.current = new Current();

            weatherObj.current.temp_c = currentJson.getDouble("temp_c");
            weatherObj.current.temp_f = currentJson.getDouble("temp_f");
            weatherObj.current.wind_mph = currentJson.getDouble("wind_mph");
            weatherObj.current.wind_kph = currentJson.getDouble("wind_kph");
            weatherObj.current.precip_mm = currentJson.getDouble("precip_mm");
            weatherObj.current.precip_in = currentJson.getDouble("precip_in");

            return weatherObj;
        } else {
            throw new WeatherException("Não foi possível encontrar a cidade " + cidade);
        }
    }

    public static final String get(String cidade) throws WeatherException {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(URL + "&q=" + cidade + "&aqi=no");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        } catch (MalformedURLException | ProtocolException ex) {
            throw new WeatherException(ex.getMessage());
        } catch (IOException ex) {
            throw new WeatherException(ex.getMessage());
        } catch (Exception ex) {
            throw new WeatherException(ex.getMessage());
        }

        return result.toString();
    }
}
