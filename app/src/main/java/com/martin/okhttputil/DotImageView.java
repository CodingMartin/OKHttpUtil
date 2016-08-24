package com.martin.okhttputil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class DotImageView extends ImageView {

    private String mDotText;
    private int mDotTextColor;
    private int mDotColor;
    private float mDotTextSize = 16;
    private boolean mDotVisible;
    private int mDotPadding;
    private float mDotInnerPadding;
    private float mDotSize;
    private Paint mPaint;
    private int textWidth;
    private Rect mRect;

    public DotImageView(Context context) {
        this(context, null);
    }

    public DotImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.DotImageView, defStyleAttr, 0);
        mDotText = a.getString(R.styleable.DotImageView_dotText);
        mDotTextColor = a.getColor(R.styleable.DotImageView_dotTextColor, Color.WHITE);
        mDotColor = a.getColor(R.styleable.DotImageView_dotColor, Color.RED);
        mDotSize = a.getDimension(R.styleable.DotImageView_dotSize, 15);
        mDotVisible = a.getBoolean(R.styleable.DotImageView_dotVisible, true);
        mDotPadding = a.getDimensionPixelSize(R.styleable.DotImageView_dotPadding, 5);
        mDotInnerPadding = a.getDimension(R.styleable.DotImageView_dotInnerPadding, 2);
        a.recycle();


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setTextSize(mDotTextSize);
        mRect = new Rect();
        resizeTextSize();
    }

    private void resizeTextSize() {
        Paint.FontMetrics metrics = new Paint.FontMetrics();


        if (!TextUtils.isEmpty(mDotText)) {
            int availableWidth = (int) (mDotSize - 2 * mDotInnerPadding);
            mPaint.getTextBounds(mDotText, 0, mDotText.length(), mRect);
            int textSize = Math.max(mRect.width(), mRect.height());
            while (textSize > availableWidth) {
                mDotTextSize--;
                mPaint.setTextSize(mDotTextSize);
                mPaint.getTextBounds(mDotText, 0, mDotText.length(), mRect);
                textSize = Math.max(mRect.width(), mRect.height());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            int centerX = getMeasuredWidth() / 2;
            int centerY = getMeasuredHeight() / 2;
            int xPadding = mDotPadding;
            int avalibleWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int dotXNeed = (int) (width / 2 + centerX + xPadding + mDotSize);
            while (avalibleWidth < dotXNeed) {
                xPadding = xPadding - 1;
                dotXNeed = (int) (width / 2 + centerX + xPadding + mDotSize);
            }

            int yPadding = mDotPadding;
            int avalibleHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
            int dotYNeed = (int) (centerY + height / 2 + yPadding + mDotSize);
            while (dotYNeed > avalibleHeight) {
                yPadding = yPadding - 1;
                dotYNeed = (int) (centerY + height / 2 + yPadding + mDotSize);
            }
            mPaint.setColor(mDotColor);
            float x = width + xPadding + mDotSize / 2;
            float y = centerY - (centerY + yPadding + mDotSize / 2);
            canvas.drawCircle(x, y, mDotSize / 2, mPaint);

            if (!TextUtils.isEmpty(mDotText)) {
                mPaint.setColor(mDotTextColor);
                Paint.FontMetrics mFontMetrics = mPaint.getFontMetrics();
                mPaint.getTextBounds(mDotText, 0, mDotText.length(), mRect);
                float tx = x - mDotSize / 2 + (mDotSize - mRect.width()) / 2;
                float ty = y - (mRect.height()) / 2 + Math.abs(mFontMetrics.ascent) ;
                canvas.drawText(mDotText, tx, ty, mPaint);
            }

//            canvas.drawCircle(width + xPadding + mDotSize / 2, mDotSize / 2, mDotSize / 2, mPaint);
        }


    }
}
