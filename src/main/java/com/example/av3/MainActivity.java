package com.example.av3;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.av3.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import Database.WeatherDAO;
import Entities.WeatherCache;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private WeatherDAO dao;

    private TextView CityText;
    private TextView TempText;
    private TextView PrecipText;
    private TextView WindText;
    private TextView LatText;
    private TextView LonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new WeatherDAO(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        CityText = (TextView)this.findViewById(R.id.city_text);
        TempText = (TextView)this.findViewById(R.id.temp_text);
        PrecipText = (TextView)this.findViewById(R.id.precip_text);
        WindText = (TextView)this.findViewById(R.id.vento_text);
        LatText = (TextView)this.findViewById(R.id.lat_text);
        LonText = (TextView)this.findViewById(R.id.lon_text);

        setActiveCache();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "todo reload info from API or Database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setActiveCache() {
        int id = dao.getActiveWeather();

        if (id != -1) {
            WeatherCache cache = dao.GetById(id);
            CityText.setText(cache.Name);
            TempText.setText(cache.TempC + " °C");
            PrecipText.setText(cache.PrecipMm + " mm");
            WindText.setText(cache.WindMph + " Mph");
            LatText.setText(cache.Latitude + "°");
            LonText.setText(cache.Longitude + "°");
        } else {
            CityText.setText("--");
            TempText.setText("--" + " °C");
            PrecipText.setText("--" + " mm");
            WindText.setText("--" + " Mph");
            LatText.setText("--" + "°");
            LonText.setText("--" + "°");
        }
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
        setActiveCache();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setActiveCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}