package fr.corentinpacaud.shortcut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Corentin on 11/01/2017.
 */

public class CircleBarView extends View {

    private static final String LOG_TAG = CircleBarView.class.getSimpleName();

    Paint mPaint;
    Handler mHandler;
    Runnable mRunnable;
    int mNbBar = 20;
    double[] mPreviusRadius;
    double[] mSpeed;
    boolean[] mDirection;
    double angle;
    double mFps = 60;
    double mMinSize = 0.5d;

    boolean isPlaying = false;

    public CircleBarView(Context context) {
        super(context);
        init();
    }

    public CircleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CircleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.holo_red_dark));
        mPaint.setStrokeWidth(30f);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                postInvalidate();
            }
        };
        mPreviusRadius = new double[mNbBar];
        mSpeed = new double[mNbBar];
        mDirection = new boolean[mNbBar];

        for (int i = 0; i < mPreviusRadius.length; i++) {
            mPreviusRadius[i] = mMinSize;
            Log.d(LOG_TAG, "rad : " + mPreviusRadius[i]);
            mSpeed[i] = (Math.random() + 1d) * 3d;
            mDirection[i] = (Math.random() * 2) > 1;
        }

        angle = Math.PI * 2d / mNbBar;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        double radius = (Math.min(getWidth(), getHeight()) / 2);

        boolean end = true;


        for (int i = 0; i < mNbBar; i++) {

            if (mPreviusRadius[i] < 0.5) {
                mDirection[i] = true;
            }
            if (mPreviusRadius[i] > 1) {
                mDirection[i] = false;
            }

            if (!isPlaying) {
                mDirection[i] = false;
            }

            float destX = (float) ((float) Math.cos(angle * i) * radius * mPreviusRadius[i] + centerX);
            float destY = (float) ((float) Math.sin(angle * i) * radius * mPreviusRadius[i] + centerY);

            canvas.drawLine(centerX, centerY, destX, destY, mPaint);
            canvas.drawCircle(centerX, centerY, ((float) radius) * 0.5f, mPaint);

            mPreviusRadius[i] = mPreviusRadius[i] + (1 / mFps) * mSpeed[i] * (mDirection[i] ? 1 : -1);
            if (!isPlaying && mPreviusRadius[i] > mMinSize)
                end = false;
        }

        if (isPlaying || !end)
            mHandler.postDelayed(mRunnable, (long) (1000 / mFps));
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        isPlaying = true;
        postInvalidate();
    }

    public void stop() {
        isPlaying = false;
        postInvalidate();
    }
}
