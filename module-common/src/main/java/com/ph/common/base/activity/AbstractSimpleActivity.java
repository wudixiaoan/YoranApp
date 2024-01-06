package com.ph.common.base.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ph.common.base.ActivityCollector;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AbstractSimpleActivity extends AppCompatActivity {
    protected AbstractSimpleActivity mContext;
    //ButterKnife
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ActivityCollector.getInstance().addActivity(getPackageName() + "." + this.getLocalClassName(), this);
        ActionBar actionBar = getSupportActionBar();
        if (hideActionbar()) {
            actionBar.hide();
        }
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        onViewCreated();
        if (isKeepScreenOn()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        initEventAndData();
    }

    private boolean hideActionbar() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFullScreen()) {
            setFullScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.getInstance().removeActivity(getPackageName() + "." + this.getLocalClassName());
        mUnbinder.unbind();
        mUnbinder = null;
    }

    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * view 的创建 留给子类实现
     */
    protected abstract void onViewCreated();

    /**
     * 初始化数据留给子类实现
     */
    protected abstract void initEventAndData();

    /**
     * 获取布局对象 留给子类实现
     */
    protected abstract int getLayout();

    /**
     * 是否屏幕常亮 重写该方法 并返回true
     * 默认true
     *
     * @return
     */
    protected boolean isKeepScreenOn() {
        return true;
    }

    /**
     * 是否全屏
     *
     * @return
     */
    protected boolean isFullScreen() {
        return false;
    }

    /**
     * 是否屏蔽返回键
     *
     * @return
     */
    protected boolean isBackEnable() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isBackEnable()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
