package com.br.relogiodecabeceira;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ViewHolder viewHolder = new ViewHolder();
    private Handler handler = new Handler();
    private Runnable runnable;
    private Boolean runnableStopped = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        viewHolder.TextHourMinute = findViewById(R.id.text_hour_minute);
        viewHolder.TextSeconds = findViewById(R.id.text_seconds);
        viewHolder.CheckBattery = findViewById(R.id.check_battery);
        viewHolder.textBatteryLevel = findViewById(R.id.text_battery_level);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        registerReceiver(batteryreceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }

    private static class ViewHolder {

        TextView TextHourMinute;
        TextView TextSeconds;
        CheckBox CheckBattery;
        TextView textBatteryLevel;


    }

    private void startBedside() {

        final Calendar calendar = Calendar.getInstance();

        runnable = new Runnable() {
            @Override
            public void run() {

                if (runnableStopped)
                    return;


                calendar.setTimeInMillis(System.currentTimeMillis());

                String hourMinutesFormat = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
                String secondsFormat = String.format("%02d", calendar.get(Calendar.SECOND));

                viewHolder.TextHourMinute.setText(hourMinutesFormat);
                viewHolder.TextSeconds.setText(secondsFormat);

                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - (now % 1000));


                handler.postAtTime(runnable, next);


            }
        };
        runnable.run();


    }

    @Override
    public void onResume() {

        startBedside();
        runnableStopped = false;
        super.onResume();

    }

    @Override
    protected void onStop() {

        runnableStopped = true;
        super.onStop();
    }

    private BroadcastReceiver batteryreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            viewHolder.textBatteryLevel.setText(String.valueOf(level) + "%");


        }
    };
}
