package com.example.av3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.av3.databinding.FragmentFirstBinding;

import Database.WeatherDAO;
import Entities.WeatherCache;

public class FirstFragment extends Fragment {

    private WeatherDAO dao;

    private TextView CityText;
    private TextView TempText;
    private TextView PrecipText;
    private TextView WindText;
    private TextView LatText;
    private TextView LonText;

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dao = new WeatherDAO(view.getContext());

        CityText = (TextView)view.findViewById(R.id.city_text);
        TempText = (TextView)view.findViewById(R.id.temp_text);
        PrecipText = (TextView)view.findViewById(R.id.precip_text);
        WindText = (TextView)view.findViewById(R.id.vento_text);
        LatText = (TextView)view.findViewById(R.id.lat_text);
        LonText = (TextView)view.findViewById(R.id.lon_text);

        setActiveCache();

        binding.reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
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
            System.out.println(cache.Name);
        } else {
            CityText.setText("--");
            TempText.setText("-- °C");
            PrecipText.setText("-- mm");
            WindText.setText("-- Mph");
            LatText.setText("-- °");
            LonText.setText("-- °");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}