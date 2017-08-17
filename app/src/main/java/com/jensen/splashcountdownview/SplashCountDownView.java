package com.jensen.splashcountdownview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/8/17.
 */

public class SplashCountDownView extends View {
    private static final String TAG = "SplashCountDownView";
    private Paint mPaint_arc;
    private Paint mPaint_circle;
    private Paint mPaint_text;
    private float innerCircleWidth;//内圆宽度
    private int innerCircleColor;//内圆颜色
    private float innerCircleRadius;//内圆半径
    private float arcWidth;//进度弧度宽度
    private int arcColor;//进度弧度颜色
    private float countDownTextSize;//文字大小
    private int countDownTextColor;//文字颜色
    private int startOrientation;//进度开始方向
    private float startDegree;//进度开始的初始角度
    private float currentDegree;//当前进度角度
    private int width;//view宽
    private int height;
    private String currentText = "2";

    public SplashCountDownView(Context context) {
        this(context, null, 0);
    }

    public SplashCountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取资源参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SplashCountDownView);

        innerCircleWidth = typedArray.getDimension(R.styleable.SplashCountDownView_inner_circle_width,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        innerCircleColor = typedArray.getColor(R.styleable.SplashCountDownView_inner_circle_color, Color.GRAY);
        innerCircleRadius = typedArray.getDimension(R.styleable.SplashCountDownView_inner_circle_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        arcWidth = typedArray.getDimension(R.styleable.SplashCountDownView_arc_width,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        arcColor = typedArray.getColor(R.styleable.SplashCountDownView_arc_color, Color.BLUE);
        countDownTextSize = typedArray.getDimension(R.styleable.SplashCountDownView_count_down_text_size,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        countDownTextColor = typedArray.getColor(R.styleable.SplashCountDownView_count_down_text_color, Color.GRAY);
        startOrientation = typedArray.getInt(R.styleable.SplashCountDownView_start_orientation, 3);
        startDegree = getStartDegree(startOrientation);
        typedArray.recycle();
        initPaints();
    }


    private void initPaints() {
        mPaint_circle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_circle.setStyle(Paint.Style.STROKE);
        mPaint_circle.setStrokeWidth(innerCircleWidth);
        mPaint_circle.setColor(innerCircleColor);

        mPaint_arc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_arc.setStyle(Paint.Style.STROKE);
        mPaint_arc.setStrokeWidth(arcWidth);
        mPaint_arc.setColor(arcColor);
        mPaint_arc.setStrokeCap(Paint.Cap.ROUND);//设置圆帽形

        mPaint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_text.setStyle(Paint.Style.FILL);
        mPaint_text.setTextSize(countDownTextSize);
        mPaint_text.setColor(countDownTextColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        //判断设置的圆半径是否大于宽高任意一个一半还大，则把半径置为最小的值的一半
        int min = Math.min(width, height);
        innerCircleRadius = innerCircleRadius >= min / 2 ? min / 2 - arcWidth - innerCircleWidth : innerCircleRadius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);//系统测量之后给我们的默认大小,可以不用这个值
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int resultWidth = (int) (2 * innerCircleRadius + innerCircleWidth * 2 + 2 * arcWidth);
        int resultHeight = (int) (2 * innerCircleRadius + innerCircleWidth * 2 + 2 * arcWidth);

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(resultWidth, resultHeight);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(resultWidth, sizeHeight);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(sizeWidth, resultHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float circleWidth = mPaint_circle.getStrokeWidth();
        //画圆
        canvas.drawCircle(width / 2, height / 2, innerCircleRadius, mPaint_circle);

        //画弧度
        RectF rectF = new RectF(width / 2 - innerCircleRadius - circleWidth, height / 2 - innerCircleRadius - circleWidth, (width / 2 - innerCircleRadius - circleWidth) + 2 * circleWidth + 2 * innerCircleRadius, (height / 2 - innerCircleRadius - circleWidth) + 2 * circleWidth + 2 * innerCircleRadius);
        canvas.drawArc(rectF, startDegree, currentDegree, false, mPaint_arc);

        //画文字
       /* Rect bounds = new Rect();
        mPaint_text.getTextBounds(currentText, 0, currentText.length(), bounds);*/
        float mTextWidth = mPaint_text.measureText(currentText, 0, currentText.length());
        Paint.FontMetricsInt fontMetricsInt = mPaint_text.getFontMetricsInt();
        float dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        float baseLine = height / 2 + dy;
        canvas.drawText(currentText, width / 2 - mTextWidth / 2, baseLine, mPaint_text);
    }

    private float getStartDegree(int orientation) {
        float startDegree = 0;
        switch (orientation) {
            case 0://top
                startDegree = 270f;
                break;

            case 1://left
                startDegree = 180f;
                break;

            case 2://bottom
                startDegree = 90f;
                break;

            case 3://right
                startDegree = 0f;
                break;
        }
        return startDegree;
    }

    public void startCountDown() {
        //text
        ValueAnimator valueAnimatorText = ValueAnimator.ofInt(3, 1);
        valueAnimatorText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentText = animation.getAnimatedValue() + "";
                invalidate();
            }
        });

        //arc
        ValueAnimator valueAnimatorArc = ValueAnimator.ofInt(0, 360);
        valueAnimatorArc.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentDegree = Float.parseFloat(animation.getAnimatedValue() + "");
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimatorText, valueAnimatorArc);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(3000);
        animatorSet.setStartDelay(1000);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mOnCountDownCompleteListener != null) {
                    mOnCountDownCompleteListener.complete();
                }
            }
        });
    }

    public interface OnCountDownCompleteListener {
        void complete();
    }

    private OnCountDownCompleteListener mOnCountDownCompleteListener;

    public void setOnCountDownCompleteListener(OnCountDownCompleteListener onCountDownCompleteListener) {
        mOnCountDownCompleteListener = onCountDownCompleteListener;
    }
}
