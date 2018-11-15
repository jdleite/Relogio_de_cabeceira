package com.br.relogiodecabeceira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private ViewHolder viewHolder = new ViewHolder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        viewHolder.TextHourMinute = findViewById(R.id.text_hour_minute);
        viewHolder.TextSeconds = findViewById(R.id.text_seconds);
        viewHolder.CheckBattery = findViewById(R.id.check_battery);

    }

    private static class ViewHolder{

        TextView TextHourMinute;
        TextView TextSeconds;
        CheckBox CheckBattery;

    }

}
