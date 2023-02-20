package com.example.tabipapp.UI.MinttiVision;

import android.graphics.Canvas;
import android.graphics.Paint;


import com.example.tabipapp.R;
import com.example.tabipapp.base.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccl on 2017/8/30.
 * 画PPG（光电容积脉搏波描记）波形图实例
 */

public class PPGDrawWave extends DrawWave<Integer> {

    //定义PPG波的颜色
    private final static int waveColor = App.getInstance().getResources().getColor(R.color.colorOrange);
    //定义波的线粗
    private final static float waveStrokeWidth = 5f;
    //定义X轴方向data间距
    private final static int X_INTERVAL = 2;
    private float mViewWidth;
    private float mViewHeight;
    private float dataMax;
    private float dataMin;
    private float dp;
    private Paint mWavePaint;

    public PPGDrawWave() {
        super();
        mWavePaint = newPaint(waveColor, waveStrokeWidth);
    }

    @Override
    public void initWave(float width, float height) {
        mViewWidth = width;
        mViewHeight = height;
        allDataSize = mViewWidth / X_INTERVAL;
    }

    @Override
    public void clear() {
        super.clear();
        dataMax = dataMin = 0;
        dp = 0f;
    }

    @Override
    public void drawWave(Canvas canvas) {
        final List<Integer> list = new ArrayList<>(dataList);
        int size = list.size();
        if (size >= 2) {
            dataMax = dataMin = list.get(0);
            for (int i = 0; i < size; i++) {
                try {
                    float dataI = list.get(i);
                    if (dataI < dataMin) {
                        dataMin = dataI;
                    }
                    if (dataI > dataMax) {
                        dataMax = dataI;
                    }
                } catch (NullPointerException e) {
                    e.fillInStackTrace();
                }
            }
            dp = (dataMax - dataMin) / (mViewHeight - mViewHeight / 10 * 2);
            if (dp == 0) {
                dp = 1f;
            }
            for (int i = 0; i < size - 1; i++) {
                Integer ppgDataCurr;
                Integer ppgDataNext;
                try {
                    ppgDataCurr = list.get(i);
                } catch (IndexOutOfBoundsException e) {
                    ppgDataCurr = list.get(i - 1);
                }
                try {
                    ppgDataNext = list.get(i + 1);
                } catch (IndexOutOfBoundsException e) {
                    ppgDataNext = list.get(i);
                }
                float x1 = getX(i, size);
                float x2 = getX(i + 1, size);
                float y1 = getY(ppgDataCurr);
                float y2 = getY(ppgDataNext);
                canvas.drawLine(x1, y1, x2, y2, mWavePaint);
            }
        }
    }

    @Override
    protected float getX(int value, int size) {
        try {
            return mViewWidth - ((size - 1 - value) * X_INTERVAL);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    protected float getY(Integer ppgData) {
        try {
            return mViewHeight - mViewHeight / 10 - (ppgData - dataMin) / (dp);
        } catch (NullPointerException e) {
            return 0;
        }
    }
}
