package fr.caensup.lsts.smb116.thermometre;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

public class ThermoView extends SurfaceView implements Runnable {

    private SurfaceHolder holder;
    //Definition de la dimention de l'écran
    private int screenHeight;
    private int screenWidth;
    private Paint redpaint;
    //Hauteur du thermomettre
    private int thermometerHeigth;
    //Min et max température observable
    private int MIN_TEMP = 0;
    private int MAX_TEMP = 50;
    //Correspond à la longueur du thermomettre
    private int deltaPixelsPerDegree;
    //Largeur du thermomettre
    private int thermometerWidth;
    //Point x et y du haut gauche du thermomettre
    private int thermometerX0;
    private int thermometerY0;
    //Fond du thermomettre
    private Paint greyPaint;

    private boolean isOK;

    private Thread gameloopthread;
    private final long fps = (long)(1/40 * 1000);
    private int temperature;

    public ThermoView(Context context) {
        super(context);
        holder = getHolder();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        //Definition de la résolution de l'écran
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();

        //Hauteur du thermomettre
        thermometerHeigth = (int)(screenHeight * 0.7);

        //Longueur du thermomettre
        deltaPixelsPerDegree = thermometerHeigth / (MAX_TEMP - MIN_TEMP);

        //Largueur du thermomettre
        thermometerWidth = (int)(screenWidth * 0.1);

        //Points du haut gauche du thermomettre
        thermometerY0 = (int)(screenHeight * 0.9 - thermometerHeigth - screenWidth *0.1);
        thermometerX0 = (int)(screenWidth / 2 - 0.5 * screenWidth * 0.1f);

        //Fond du rectangle indiquant la temperature
        redpaint = new Paint();
        redpaint.setARGB(255,255,0,0);
        //Fond du thermomettre
        greyPaint = new Paint();
        greyPaint.setARGB(255,50,50,50);
    }

    public void setTemperature (int temp) {

        this.temperature = temp;
        Log.i("TEMPERATURE", "Temperature : " + temp);
    }

    public void drawthermometer() {
        if(holder.getSurface().isValid()) {
            Canvas c = holder.lockCanvas();
            c.drawARGB(255,150,150,10);

            //Dessin du cercle
            c.drawCircle(screenWidth / 2, screenHeight *0.9f, screenWidth * 0.1f, redpaint);
            Paint greyPaint = new Paint();
            //Dessin thermomettre en gris
            c.drawRect(new Rect(thermometerX0, thermometerY0, thermometerX0 + thermometerWidth, thermometerY0 + thermometerHeigth), greyPaint);

            //Coordonnée y de la messure de la température
            int yTemp = thermometerY0 + ((MAX_TEMP - MIN_TEMP) - temperature) * deltaPixelsPerDegree;

            //Dessin température a partir de yTemp
            c.drawRect(new Rect(thermometerX0, yTemp, thermometerX0 + thermometerWidth, yTemp + (temperature-MIN_TEMP) * deltaPixelsPerDegree), redpaint);

            holder.unlockCanvasAndPost(c);
        }
    }

    public void resume() {
        isOK = true;
        gameloopthread = new Thread(this);
        gameloopthread.start();
    }

    public void pause() {
       isOK = false;
       gameloopthread = null;
    }

    @Override
    public void run() {
        long startTime;
        long endTime;
        long spentTime;
        while (isOK == true) {
            startTime = System.currentTimeMillis();
            drawthermometer();
            endTime = System.currentTimeMillis();
            spentTime = endTime - startTime;
            try {
                long rest = fps - spentTime;

                //Si affichage plus rapide que le fps, on attend
                if(rest > 0) Thread.sleep(rest);
            } catch (InterruptedException e) {
            }
        }
    }
}
