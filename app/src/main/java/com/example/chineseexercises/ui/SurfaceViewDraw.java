package com.example.chineseexercises.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by J on 2017/9/6.
 */

public class SurfaceViewDraw extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "SurfaceViewDrawBoard";
    private SurfaceViewDraw.MyThread myThread;
    private Bitmap cacheBitmap;
    private Canvas cacheCanvas;
    private Paint paint;
    private Path path;
    private float lastX, lastY;
    public boolean surfaceChangedbool=false;
    public SurfaceViewDraw(Context context, int width, int height) {
        super(context);
        cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setARGB(130,232,232,232);
        paint.setAntiAlias(true);
        paint.setDither(true);
        path = new Path();
        getHolder().addCallback(this);
        myThread = new SurfaceViewDraw.MyThread(getHolder());
        Log.v(TAG, Thread.currentThread().getName());
    }

    public void setActionDown(float x, float y){
        lastX = x;
        lastY = y;
        path.moveTo(x, y);
    }
    public void setActionMove(float x,float y){
        path.quadTo(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }
    public void setActionUp(){
        cacheCanvas.drawPath(path, paint);
        path.reset();
    }
    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        Log.v(TAG, "surfaceCreated");
        myThread.isRun = true;
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int h){
        Log.v(TAG, "surfaceChanged");
        surfaceChangedbool=true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "surfaceDestoryed");
        myThread.isRun = false;
    }

    class MyThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun = false;
        private int red = 0, green = 0, blue = 0;
        private int colorValue = 0;
        private int sleepSpan = 1;
        Canvas canvas;
        MyThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            while (isRun) {
                Log.v(TAG, Thread.currentThread().getName());
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    canvas.drawPath(path, paint);
                }
                try {
                    Thread.sleep(sleepSpan);
                    if(canvas!=null){
                        holder.unlockCanvasAndPost(canvas);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}