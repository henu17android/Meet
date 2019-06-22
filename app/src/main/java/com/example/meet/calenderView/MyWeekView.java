package com.example.meet.calenderView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.meet.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

/**
 * 周视图
 */
public class MyWeekView extends WeekView {

    private Paint mProgressPaint = new Paint();
    private Paint mNoneProgressPaint = new Paint();
    private int mRadius;

    @SuppressLint("ResourceAsColor")
    public MyWeekView(Context context) {
        super(context);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(dipToPx(context,2.2f));
        mProgressPaint.setColor(0xBBf54a00);

        mNoneProgressPaint.setAntiAlias(true);
        mNoneProgressPaint.setStyle(Paint.Style.STROKE);
        mNoneProgressPaint.setStrokeWidth(dipToPx(context,2.2f));
        mNoneProgressPaint.setColor(0x90CfCfCf);

    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemHeight,mItemWidth)/11 * 4 ;
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth /2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx,cy,mRadius,mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
       int cx = x +mItemWidth /2 ;
       int cy = mItemHeight /2;

       int angle = getAngle(Integer.parseInt(calendar.getScheme()));

        RectF progressRectF = new RectF(cx-mRadius,cy-mRadius,cx+mRadius,cy+mRadius);
        canvas.drawArc(progressRectF, -90,angle,false,mProgressPaint);

        RectF noneRectF = new RectF(cx-mRadius,cy-mRadius,cx+mRadius,cy+mRadius);
        canvas.drawArc(noneRectF,angle-90,360-angle,false,mNoneProgressPaint);

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    private static int getAngle(int progress){
        return  (int)(progress*3.6);
    }


    /*
   dp转px
    */
    private static int dipToPx(Context context,float dpValue){
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int)  (dpValue * scale+0.5f);
    }
}
