package com.heiwanstudio.androidgame01;

import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Observer;

public class GameLoop extends  Thread{
    private static final double MAX_UPS = 70.00;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private Game game;
    private SurfaceHolder surfaceHolder;
    private boolean isRunning = false;
    private double avarageUPS;
    private double avarageFPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAvarageUPS() {
        return avarageUPS;
    }

    public double getAvarageFPS() {
        return avarageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long ellapsedTime;
        long sleepTime;

        startTime = System.currentTimeMillis();
        Canvas canvas = null;
        //try to update and render game
        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;

                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //pause to not exceed target UPS
            ellapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long)(updateCount *UPS_PERIOD - ellapsedTime);
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //skip frames to keep up with target FPS
            while (sleepTime < 0 && updateCount < MAX_UPS -1) {
                game.update();
                updateCount++;
                ellapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long)(updateCount *UPS_PERIOD - ellapsedTime);
            }

            //calculate avg UPS AND FPS
            if (ellapsedTime >= 1000) {
                avarageUPS = updateCount / (1E-3 * ellapsedTime);
                avarageFPS = frameCount / (1E-3 * ellapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
}
