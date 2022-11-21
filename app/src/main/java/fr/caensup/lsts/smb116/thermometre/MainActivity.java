package fr.caensup.lsts.smb116.thermometre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ThermoView thermoView;
    private BroadcastReceiver temperaturebroadcastreceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thermoView = new ThermoView(this);
        setContentView(thermoView);
        startService(new Intent(MainActivity.this, TemperatureService.class));

        temperaturebroadcastreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int temperature = intent.getIntExtra("temperature", 10);
                thermoView.setTemperature(temperature);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        thermoView.resume();
        registerReceiver(temperaturebroadcastreceiver, new IntentFilter(TemperatureService.ACTION_CURRENT_TEMPERATURE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        thermoView.pause();
        unregisterReceiver(temperaturebroadcastreceiver);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, TemperatureService.class));
        super.onDestroy();
    }
}