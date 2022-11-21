package fr.caensup.lsts.smb116.thermometre;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TemperatureService extends Service {

    public static final String ACTION_CURRENT_TEMPERATURE =
            "fr.caensup.lsts.smb116.thermometre.temperatureservice";

    private boolean isActive = false;
    private Thread captorThread;
    private ITemperatureCaptor temperatureCaptor = null;

    public TemperatureService() {
        temperatureCaptor = new TemperatureCaptorMock(10);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isActive = true;
        startTemperatureCaptor();
    }

    @Override
    public void onDestroy() {
        isActive = false;
        super.onDestroy();

    }

    public void startTemperatureCaptor() {
        captorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    broadcastTemperature();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        captorThread.start();
    }

    private void broadcastTemperature() {
        int temperature = temperatureCaptor.getTemperature();
        Intent intent = new Intent(ACTION_CURRENT_TEMPERATURE);
        intent.putExtra("temperature", temperature);
        sendBroadcast(intent);
    }
}