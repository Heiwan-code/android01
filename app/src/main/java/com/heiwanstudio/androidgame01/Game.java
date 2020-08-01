package com.heiwanstudio.androidgame01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Player player;
    private GameLoop gameLoop;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        //init player
        player = new Player(100,0);
        //draw map
        
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

        player.draw(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String avarageUPS = Double.toString(gameLoop.getAvarageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.mantis);
        paint.setColor(color);
        paint.setTextSize(42);
        canvas.drawText("UPS: " + avarageUPS, 60, 80, paint);
    }

    public void drawFPS(Canvas canvas) {
        String avarageFPS = Double.toString(gameLoop.getAvarageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.mantis);
        paint.setColor(color);
        paint.setTextSize(42);
        canvas.drawText("FPS: " + avarageFPS, 60, 190, paint);
    }

    public void update() {
        player.update();
    }
}
