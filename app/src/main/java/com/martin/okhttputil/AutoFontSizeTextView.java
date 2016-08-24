package com.martin.okhttputil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/26
 */
public class AutoFontSizeTextView extends TextView {
    private Rect mRect;
    private boolean needChange;

    public AutoFontSizeTextView(Context context) {
        super(context);
        init();
    }

    public AutoFontSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoFontSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        needChange = !(widthMeasureSpec == MeasureSpec.AT_MOST || heightMeasureSpec == MeasureSpec.AT_MOST);
    }

    private void verifyTextSize() {
        String mText = getText().toString();
        if (TextUtils.isEmpty(mText)) return;
        int width = getWidth();
        int aWidth = width - getPaddingLeft() - getPaddingRight();
        Paint mPaint = getPaint();
        float textSize = getTextSize();
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);
        int textWidth = mRect.width();
        while (aWidth < textWidth) {
            textSize--;
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            textWidth = mRect.width();
            setTextSize(textSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needChange)
            verifyTextSize();
        super.onDraw(canvas);
    }
}
