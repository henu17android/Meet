package com.example.meet.calenderView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 日历月视图
 */
public class MyMonthView extends MonthView {

    private Paint mProgressPaint = new Paint();
    private Paint mNoneProgressPaint = new Paint();
    private int mRadius;

    public MyMonthView(Context context) {
        super(context);
        //抗锯齿
        mProgressPaint.setAntiAlias(true);
        //只描边不填充
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(dipToPx(context,2.2f));
        mProgressPaint.setColor(0xBB8A2BE2);

        mNoneProgressPaint.setAntiAlias(true);
        mNoneProgressPaint.setStyle(Paint.Style.STROKE);
        mNoneProgressPaint.setStrokeWidth(dipToPx(context,2.2f));
        mNoneProgressPaint.setColor(0x90CfCfCf);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth,mItemHeight)/11*4;
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight/2;
        canvas.drawCircle(cx,cy,mRadius,mSelectedPaint);
        return false;
    }

    //绘制标记事件的日子
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth  /2;
        int cy = mItemHeight /2;

        int angle = getAngle(Integer.parseInt(calendar.getScheme()));
        //确定绘制的范围
        RectF progressRectF = new RectF(cx - mRadius,cy -mRadius,cx + mRadius,cy + mRadius);
        //绘制已完成进度的圆弧
        canvas.drawArc(progressRectF,-90,angle,false,mProgressPaint);

        RectF noneRectF = new RectF(cx - mRadius,cy - mRadius,cx + mRadius,cy+mRadius);
        //绘制未完成进度的圆弧
        canvas.drawArc(noneRectF,angle-90,360-angle,false,mNoneProgressPaint);

    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth/2;

        if (isSelected){
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        }else if (hasScheme){
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint:
                            calendar.isCurrentMonth()?mSchemeTextPaint: mOtherMonthTextPaint);
        }else{
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay()?mCurDayTextPaint :
                            calendar.isCurrentMonth()?mCurMonthTextPaint : mOtherMonthTextPaint);
        }



    }



    /**
     * 获取角度
     *
     * @param progress 进度
     * @return 获取角度
     */
    private static int getAngle(int progress){
        return (int) ( progress * 3.6);
    }



    /*
    dp转px
     */
    private static int dipToPx(Context context,float dpValue){
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int)  (dpValue * scale+0.5f);
    }

}
