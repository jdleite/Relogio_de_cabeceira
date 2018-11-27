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
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder viewHolder = new ViewHolder();
    private Handler handler = new Handler();
    private Runnable runnable;
    private Boolean runnableStopped = false;
    private boolean mIsBatteryOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        viewHolder.TextHourMinute = findViewById(R.id.text_hour_minute);
        viewHolder.TextSeconds = findViewById(R.id.text_seconds);
        viewHolder.CheckBattery = findViewById(R.id.check_battery);
        viewHolder.textBatteryLevel = findViewById(R.id.text_battery_level);
        viewHolder.imageOption = findViewById(R.id.image_options);
        viewHolder.imageClose = findViewById(R.id.image_close);
        viewHolder.linearOptions = findViewById(R.id.linear_options);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        registerReceiver(batteryreceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        viewHolder.CheckBattery.setChecked(true);

        viewHolder.linearOptions.animate().translationY(500);

        setListener();


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.check_battery) {
            toggleCheckBaterry();
        } else if (id == R.id.image_options) {
            viewHolder.linearOptions.setVisibility(View.VISIBLE);
            viewHolder.linearOptions.animate().translationY(0).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));


        } else if (id == R.id.image_close) {
            viewHolder.linearOptions.setVisibility(View.VISIBLE);
            viewHolder.linearOptions.animate().translationY(viewHolder.linearOptions.getMeasuredHeight()).setDuration(getResources()
                    .getInteger(android.R.integer.config_mediumAnimTime));


        }


    }

    private void toggleCheckBaterry() {
        if (mIsBatteryOn) {
            mIsBatteryOn = false;
            viewHolder.textBatteryLevel.setVisibility(View.GONE);
        } else {
            mIsBatteryOn = true;
            viewHolder.textBatteryLevel.setVisibility(View.VISIBLE);

        }
    }

    private static class ViewHolder {

        TextView TextHourMinute;
        TextView TextSeconds;
        CheckBox CheckBattery;
        TextView textBatteryLevel;
        ImageView imageOption;
        ImageView imageClose;
        LinearLayout linearOptions;


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

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            viewHolder.textBatteryLevel.setText(String.valueOf(level) + "%");


        }
    };

    private void setListener() {
        viewHolder.CheckBattery.setOnClickListener(this);

        viewHolder.imageOption.setOnClickListener(this);
        viewHolder.imageClose.setOnClickListener(this);


    }
}
