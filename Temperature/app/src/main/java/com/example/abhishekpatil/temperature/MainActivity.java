package com.example.abhishekpatil.temperature;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public TextView textView;
    private LinearLayout back;
    public TextView heading;
    private ProgressDialog pb;
    AlertDialog.Builder alertDialog;
    private static final String APIKEY = "5753db64c667d9ed89962813328c19be";
    private InterfaceAPI service;
    private String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back = (LinearLayout)findViewById(R.id.back);
        textView = (TextView) findViewById(R.id.text);
        heading = (TextView)findViewById(R.id.head);
        //progressbar
        pb = new ProgressDialog(MainActivity.this);
        pb.setMessage("Loading...");
        pb.setTitle("WEATHER");
        pb.show();



        heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertdialog
                alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("CITY");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("Done",new Dialog.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String city1 = input.getText().toString();
                        if (city1 ==null){
                            Toast.makeText(getApplicationContext(),"data not Available",Toast.LENGTH_LONG).show();
                            city = "delhi";
                        }else
                        {
                            city = city1;
                        }
                        getWeather(city);
                    }
                });
                alertDialog.show();
            }
        });

        service = ClientAPI.getRetrofitClient().create(InterfaceAPI.class);
        getWeather("Delhi");
    }

    private void getWeather(String city ) {
        if(city == null){
            city = "delhi";
        }
        final String finalCity = city.substring(0,1).toUpperCase() + city.substring(1);
        service.getdata(APIKEY, city).enqueue(new Callback<Weather>() {
            private Main main;
            private Clouds clouds;

            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(response.body()!=null){
                    main = response.body().getMain();
                    int temp = (int) (main.getTemp() - 273.15);
                    textView.setText(String.valueOf(temp) + "°C");
                    heading.setText(finalCity);
                    if(temp>45){
                        back.setBackgroundColor(getResources().getColor(R.color.VeryHot));
                    }else if(temp>35){
                        back.setBackgroundColor(getResources().getColor(R.color.Hot));
                    }
                    else if(temp>25){
                        back.setBackgroundColor(getResources().getColor(R.color.Normal));
                    }
                    else if(temp>15){
                        back.setBackgroundColor(getResources().getColor(R.color.cold));
                    }
                    else {
                        back.setBackgroundColor(getResources().getColor(R.color.VeryCold));
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"data not available",Toast.LENGTH_LONG).show();
                }
                pb.cancel();

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                pb.cancel();
                Toast.makeText(getApplicationContext(), "Data not available", Toast.LENGTH_LONG).show();
            }
        });
    }

}
