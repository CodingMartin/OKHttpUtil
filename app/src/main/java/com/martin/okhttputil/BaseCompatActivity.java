package com.martin.okhttputil;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class BaseCompatActivity extends AppCompatActivity {
    @Nullable
    private HansToolbar mToolbar;
    @Nullable
    private TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = findView(R.id.toolbar);
        mTitleView = mToolbar.getTitleTextView();
    }

    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }
}
