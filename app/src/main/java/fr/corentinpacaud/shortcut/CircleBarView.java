package fr.corentinpacaud.shortcut;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
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
    int mNbBar;
    double[] mPreviusRadius;
    double[] mSpeed;
    boolean[] mDirection;
    Paint[] mBarPaint;
    double angle;
    double mFps = 60;

    double mMinSize;
    double mMaxSpeed;
    double mMinSpeed;

    int mMaxColor;
    /**
     * See
     */
    int mMinColor;

    boolean isPlaying = false;

    public CircleBarView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CircleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircleBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleBarView,
                0, 0);

        try {

            mNbBar = a.getInt(R.styleable.CircleBarView_nbBars, 10);
            mMinSize = a.getDimensionPixelSize(R.styleable.CircleBarView_minSize, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mMinColor = a.getColor(R.styleable.CircleBarView_minColor, getResources().getColor(R.color.colorAccent, null));
            } else {
                //noinspection deprecation
                mMinColor = a.getColor(R.styleable.CircleBarView_minColor, getResources().getColor(R.color.colorAccent));
            }
            mMaxColor = a.getColor(R.styleable.CircleBarView_minColor, mMinColor);

            mMinSpeed = a.getFloat(R.styleable.CircleBarView_minSpeed, 1f);
            mMaxSpeed = a.getFloat(R.styleable.CircleBarView_maxSpeed, 2f);


        } finally {
            a.recycle();
        }

        mPaint = new Paint();
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
        mBarPaint = new Paint[mNbBar];

        for (int i = 0; i < mPreviusRadius.length; i++) {
            mPreviusRadius[i] = mMinSize;
            Log.d(LOG_TAG, "rad : " + mPreviusRadius[i]);
            mSpeed[i] = randomSpeed();
            mDirection[i] = (Math.random() * 2) > 1;

            mBarPaint[i] = new Paint();
            mBarPaint[i].setStrokeWidth(30f);
        }

        angle = Math.PI * 2d / mNbBar;
    }

    private double randomSpeed() {
        return (Math.random() + mMinSpeed) * mMaxSpeed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(LOG_TAG, "W:" + (getWidth() / 2) + "  minSize:" + mMinSize);

        if (getWidth() / 2 < mMinSize) {
            throw new IllegalArgumentException("minSize is too big, it should be lower than half the size of the CircleBarView");
        }

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        double radius = (Math.min(getWidth(), getHeight()) / 2);

        boolean end = true;


        for (int i = 0; i < mNbBar; i++) {

            // Min size reached, go forward
            if (mPreviusRadius[i] < mMinSize && !mDirection[i]) {
                mDirection[i] = true;
                mSpeed[i] = randomSpeed();
            }
            // Max size reached, go back
            if (mPreviusRadius[i] > 1 && mDirection[i]) {
                mDirection[i] = false;
                mSpeed[i] = randomSpeed();
            }

            // If stop, all bar go back
            if (!isPlaying) {
                mDirection[i] = false;
            }

            float destX = (float) ((float) Math.cos(angle * i) * radius * mPreviusRadius[i] + centerX);
            float destY = (float) ((float) Math.sin(angle * i) * radius * mPreviusRadius[i] + centerY);

            double percent = ((mPreviusRadius[i] * (getWidth() / 2)) - mMinSize) / ((getWidth() / 2) - mMinSize);

            if (percent < 0)
                percent = 0;
            if (percent > 1)
                percent = 1;

            Log.d(LOG_TAG, "percent : " + percent);
            int minRed = Color.red(mMinColor);
            int maxRed = Color.red(mMaxColor);
            int minBlue = Color.blue(mMinColor);
            int maxBlue = Color.blue(mMaxColor);
            int minGreen = Color.green(mMinColor);
            int maxGreen = Color.green(mMaxColor);
            int colorRedPercent = (int) (minRed * (1 - percent) + (maxRed * percent));
            int colorBluePercent = (int) (minBlue * (1 - percent) + (maxBlue * percent));
            int colorGreenPercent = (int) ((minGreen * (1 - percent) + maxGreen * percent));
            Log.d(LOG_TAG, "R:" + colorRedPercent + "  G:" + colorGreenPercent + "  B:" + colorBluePercent);

            int percentColor = Color.rgb(colorRedPercent, colorGreenPercent, colorBluePercent);
            mBarPaint[i].setColor(percentColor);

            canvas.drawLine(centerX, centerY, destX, destY, mBarPaint[i]);

            mPreviusRadius[i] = mPreviusRadius[i] + ((getWidth()/2) / mFps) * mSpeed[i] * (mDirection[i] ? 1 : -1);
            if (!isPlaying && mPreviusRadius[i] > mMinSize)
                end = false;
        }

        canvas.drawCircle(centerX, centerY, ((float) radius) * 0.5f, mPaint);

        if (isPlaying || !end)
            mHandler.postDelayed(mRunnable, (long) (1000 / mFps));
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        isPlaying = true;
        for (int i = 0; i < mDirection.length; i++)
            mDirection[i] = true;
        postInvalidate();
    }

    public void stop() {
        isPlaying = false;
        postInvalidate();
    }
}
