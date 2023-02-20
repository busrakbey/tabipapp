package com.example.tabipapp.UI.MinttiVision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ccl on 2017/8/23.
 * 信号波形图控件，用于展示测量时的瞬时信号波形图。
 * 适用于高速刷新数据的情况，大大降低控件继承View时因高速刷新造成的UI不流畅问题。
 */

public class WaveSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private final static long sleepTimeMillis = 20L;
    private boolean isLoop;
    private Thread mDrawWaveThread;
    private final SurfaceHolder mSurfaceHolder;
    protected DrawWave mDrawWave;
    private boolean isPause;

    public WaveSurfaceView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
    }

    public WaveSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
    }

    public WaveSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = getHolder();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        logger("surfaceCreated");
        isLoop = true;
        //创建并启动一个画图线程。
        mDrawWaveThread = new Thread(this);
        mDrawWaveThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        logger("surfaceChanged - width:" + width + ", height:" + height);
        mDrawWave.initWave(getWidth(), getHeight());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        logger("surfaceDestroyed");
        //销毁画图线程。
        isLoop = false;
        mDrawWaveThread = null;
    }

    @Override
    public void run() {
        while (isLoop) {
//            logger("run()");
            synchronized (mSurfaceHolder) {
                if (isPause) {
                    try {
                        mSurfaceHolder.wait();
//                        logger(" mSurfaceHolder.wait()");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Canvas canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (canvas == null) {
                    isLoop = false;
//                    logger("canvas == null, return");
                    return;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mDrawWave.drawWave(canvas);
                try {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(sleepTimeMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 恢复SurfaceView的图形刷新循环
     * 当处于暂停时有效。
     */
    public void reply() {
        if (!isPause) return;
        isPause = false;
        synchronized (mSurfaceHolder) {
            try {
                mSurfaceHolder.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停SurfaceView的图形刷新循环
     */
    public void pause() {
        isPause = true;
    }

    /**
     * 在控件初始化时，将一个DrawWave对象放进来
     */
    public <T> void setDrawWave(DrawWave<T> drawWave){
        this.mDrawWave = drawWave;
        setZOrderOnTop(true);
        setFocusable(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(this);
    }
}