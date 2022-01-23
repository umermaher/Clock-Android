package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class CustomAnalogClock extends View{
    private int mHeight;
    private int mWidth=0;                 //height and width of clock
    private int[] mClockHours={1,2,3,4,5,6,7,8,9,10,11,12};   //denote the hours
    private int mSpacing=0;                //spacing clock-hand around the clock round
    private int mPadding=0;                //padding //    //    ///   //

    // truncation of the heights of the clock-hands,
     //hour clock-hand will be smaller comparatively to others
    private int mHandTruncation, mHourHandTruncation = 0;
    private int mRadius=0;
    private Paint mPaint;
    private Rect mRect=new Rect();
    private boolean isInit;  // it will be true once the clock will be initialized.

    public CustomAnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomAnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //initialize essential values
        if(!isInit){
            mPaint=new Paint();
            mHeight=getHeight();
            mWidth=getWidth();
            mPadding=mSpacing+50;  //spacing frome the circular border
            int minAttr=Math.min(mHeight,mWidth);
            mRadius=(minAttr/2)-mPadding;

            // for maintaining different heights among the clock-hands
            mHandTruncation = minAttr/20;
            mHourHandTruncation = minAttr/17;

            isInit=true;      //set true when initialized
        }
        /** draw the canvas-color */
        //canvas.drawColor(Color.WHITE);

        mPaint.reset();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);

        canvas.drawCircle(mWidth/2,mHeight/2,mRadius+mPadding-10,mPaint);

        /** clock-center */
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 12, mPaint);  // the 03 clock hands will be rotated from this center point.

        /** border of hours */

        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);  // set font size (optional)

        for (int hour : mClockHours) {
            String tmp = String.valueOf(hour);
            mPaint.getTextBounds(tmp, 0, tmp.length(), mRect);  // for circle-wise bounding

            // find the circle-wise (x, y) position as mathematical rule
            double angle = Math.PI / 6 * (hour - 3);
            int x = (int) (mWidth / 2 + Math.cos(angle) * mRadius - mRect.width() / 2);
            int y = (int) (mHeight / 2 + Math.sin(angle) * mRadius + mRect.height() / 2);

            canvas.drawText(String.valueOf(hour), x, y, mPaint);  // you can draw dots to denote hours as alternative

        }
        /** draw clock hands to represent the every single time */

        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;

        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60) * 5f, true, false); // draw hours
        drawHandLine(canvas, calendar.get(Calendar.MINUTE), false, false); // draw minutes
        drawHandLine(canvas, calendar.get(Calendar.SECOND), false, true); // draw seconds

        /** invalidate the appearance for next representation of time  */
        postInvalidateDelayed(500);
        invalidate();

    }
    private void drawHandLine(Canvas canvas, double moment, boolean isHour, boolean isSecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius = isHour ? 300: mRadius - mHandTruncation;
        if (isSecond) mPaint.setColor(Color.RED);
        canvas.drawLine(mWidth / 2, mHeight / 2, (float) (mWidth / 2 + Math.cos(angle) * handRadius), (float) (mHeight / 2 + Math.sin(angle) * handRadius), mPaint);
    }
}
