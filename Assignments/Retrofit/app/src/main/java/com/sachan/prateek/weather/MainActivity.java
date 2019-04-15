package com.sachan.prateek.weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sachan.prateek.weather.Data.WeatherData;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String APIKEY = "f308b2139ca9071215ec0fd93173a019";
    private Weather_Service weatherService;
    private TextView city,lat,lon,country,temp,press,type,desc,humi,visibility,windSpeed,sunrise,sunset;
    private ImageView imageView;
    private static WeatherData weatherData;
    private EditText editText;
    private SimpleDateFormat format;
    private Button button;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        weatherService = Retrofit_Builder.getRetrofit().create(Weather_Service.class);
        instantiate();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData(editText.getText().toString());
                editText.selectAll();
                editText.clearFocus();
            }
        });
    }

    void fetchData(String zipCode) {
        weatherService.getWeatherData(APIKEY, zipCode + ",in").enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.body() != null) {
                    weatherData= response.body();
                    city.setText(weatherData.getName());
                    lon.setText(String.valueOf(weatherData.getCoord().getLon()));
                    lat.setText(String.valueOf(weatherData.getCoord().getLat()));
                    temp.setText(String.valueOf(weatherData.getMain().getTemp()));
                    press.setText(String.valueOf(weatherData.getMain().getPressure()));
                    humi.setText(String.valueOf(weatherData.getMain().getHumidity()));
                    visibility.setText(String.valueOf(weatherData.getVisibility()));
                    type.setText(weatherData.getWeather().get(0).getMain());
                    desc.setText(weatherData.getWeather().get(0).getDescription());
                    country.setText(weatherData.getSys().getCountry());
                    windSpeed.setText(String.valueOf(weatherData.getWind().getSpeed()));
                    date=new Date((long)weatherData.getSys().getSunrise()*1000);
                    sunrise.setText(format.format(date));
                    date=new Date((long)weatherData.getSys().getSunset()*1000);
                    sunset.setText(format.format(date));
                    Glide.with(MainActivity.this).load("http://openweathermap.org/img/w/" + response.body().getWeather().get(0).getIcon() + ".png").into(imageView);
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Postal/Zip code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
    @SuppressLint("SimpleDateFormat")
    private void instantiate(){
        imageView = findViewById(R.id.image);
        city = findViewById(R.id.text);
        lat=findViewById(R.id.lat);
        format= new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        country=findViewById(R.id.country);
        lon=findViewById(R.id.lon);
        temp=findViewById(R.id.temp);
        humi=findViewById(R.id.hum);
        press=findViewById(R.id.press);
        type=findViewById(R.id.desc);
        desc=findViewById(R.id.weather);
        visibility=findViewById(R.id.vis);
        windSpeed=findViewById(R.id.wind);
        sunrise=findViewById(R.id.sunrise);
        sunset=findViewById(R.id.sunset);
        editText = findViewById(R.id.edit);
        button = findViewById(R.id.bt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this,SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
