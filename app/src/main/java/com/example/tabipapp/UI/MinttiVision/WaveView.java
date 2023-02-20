package com.example.tabipapp.UI.MinttiVision;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.example.tabipapp.R;


/**
 * Created by ccl on 2017/8/23.
 * 信号波形图控件，用于展示信号波形图。
 * 适用于低速刷新数据或被镶嵌于HorizontalScrollView中作为历史数据波形展示。
 */

public class WaveView extends View {

    protected DrawWave mDrawWave;
    private boolean scrollable;

    public WaveView(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mDrawWave != null)
            mDrawWave.initWave(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawWave != null)
            mDrawWave.drawWave(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (scrollable && mDrawWave != null) {
            widthMeasureSpec = mDrawWave.getWidthMeasureSpec();
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public <T> void setDrawWave(DrawWave<T> drawWave) {
        mDrawWave = drawWave;
        mDrawWave.setView(this);
        requestLayout();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        scrollable = typedArray.getBoolean(R.styleable.WaveView_scrollable, false);
        typedArray.recycle();
    }
}
