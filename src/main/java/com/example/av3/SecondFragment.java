package com.example.av3;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.av3.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Controllers.WeatherController;
import Controllers.WeatherException;
import Database.DataContext;
import Database.WeatherDAO;
import Entities.WeatherCache;
import Models.Weather;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private WeatherDAO dao;
    private TextInputEditText cityInput;
    private View myView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        dao = new WeatherDAO(container.getContext());

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityInput = (TextInputEditText)view.findViewById(R.id.city_input);

        myView = view;

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String city = cityInput.getText().toString();

                new WeatherTask().execute(city);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class WeatherTask extends AsyncTask<String, Void, WeatherCache> {

        protected WeatherCache readDatabase(String city) throws ParseException {
            WeatherCache freshCache = null;
            ArrayList<WeatherCache> cache = dao.Read(city);

            for (WeatherCache c : cache) {
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(c.Localtime);
                Date now = new Date(System.currentTimeMillis());

                long nowTime = now.getTime();
                long cacheTime = date.getTime();

                long differenceInTime = Math.abs(nowTime - cacheTime);

                if ((differenceInTime / (1000 * 60)) % 60 <= 60) {
                    freshCache = c;
                    break;
                }
            }

            // todo delete old cache

            if (freshCache != null) {
                return freshCache;
            }

            return null;
        }

        @Override
        protected WeatherCache doInBackground(String... strings) {
            try {
                String city = strings[0];

                WeatherCache fresh = readDatabase(city);

                if (fresh != null) {
                    dao.SetActive(fresh.Id);
                    if (myView != null) {
                        Snackbar.make(myView, "Achamos os dados no cache que beleza!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    return fresh;
                }

                Weather weather = WeatherController.getAsObject(city);

                long rows = dao.Create(weather);

                if (rows >= 1) {
                    WeatherCache c = readDatabase(weather.location.name);
                    if(c == null){

                    }
                    dao.SetActive(c.Id);

                    if (myView != null) {
                        Snackbar.make(myView, "Opa! Pegamos da API :)", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    return c;
                }

                return null;
            } catch (WeatherException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(WeatherCache weather) {
            super.onPostExecute(weather);

            if (weather != null) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            } else {

                if (myView != null) {
                    Snackbar.make(myView, "A cidade solicitada não foi encontrado!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }
}