package com.martin.okhttputil;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class HansToolbar extends Toolbar {
    private CharSequence mTitle;
    private int mTitleColor;
    private float mTitleSize;
    private TextView mTitleTextView;
    private boolean userSystemTitle;
    private int mTitleTextAppearance;
    private int mScreenWidth;

    public HansToolbar(Context context) {
        this(context, null);
    }

    public HansToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public HansToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.HansToolbar, defStyleAttr, 0);
        mTitle = a.getText(R.styleable.HansToolbar_toolbarTitle);
        mTitleColor = a.getColor(R.styleable.HansToolbar_toolbarTitleColor, Color.WHITE);
        mTitleSize = a.getDimensionPixelSize(R.styleable.HansToolbar_toolbarTitleSize, 18);
        userSystemTitle = a.getBoolean(R.styleable.HansToolbar_userSystemTitle, false);
        a.recycle();

        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
    }

    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        if (userSystemTitle)
            super.setTitleTextAppearance(context, resId);
        else {
            mTitleTextAppearance = resId;
            if (mTitleTextView != null) {
                mTitleTextView.setTextAppearance(context, resId);
            }
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {
        if (userSystemTitle)
            super.setTitle(resId);
        else {
            setTitle(getContext().getText(resId));
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (userSystemTitle)
            super.setTitle(title);
        else {
            if (!TextUtils.isEmpty(title)) {
                if (mTitleTextView == null) {
                    final Context context = getContext();
                    mTitleTextView = new TextView(context);
                    mTitleTextView.setSingleLine();
                    mTitleTextView.setGravity(Gravity.CENTER);
                    mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                    mTitleTextView.setMaxWidth(getScreenWidth() / 2);
                    LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.gravity = Gravity.CENTER;
                    if (mTitleTextAppearance != 0) {
                        mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
                    }
                    if (mTitleColor != 0) {
                        mTitleTextView.setTextColor(mTitleColor);
                    }
                    mTitleTextView.getPaint().setTextSize(mTitleSize);
                    addView(mTitleTextView, lp);
                }
            } else if (mTitleTextView != null) {
                removeView(mTitleTextView);
            }
            if (mTitleTextView != null) {
                mTitleTextView.setText(title);
            }
            mTitle = title;
        }
    }

    @Nullable
    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    private int getScreenWidth() {
        if (mScreenWidth <= 0) {
            WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            mScreenWidth = metrics.widthPixels;
        }
        return mScreenWidth == 0 ? 200 : mScreenWidth;
    }
}
