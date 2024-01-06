package com.ph.common.utils;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ph.common.base.activity.AbstractSimpleActivity;
import com.ph.common.base.fragment.SimpleFragment;

public class ARouterUtils {
    /**
     * 根据path返回Fragment
     *
     * @param path
     * @return
     */
    public static SimpleFragment getFragment(String path) {
        return (SimpleFragment) ARouter.getInstance()
                .build(path)
                .navigation();
    }

    /**
     * 根据path返回Fragment
     *
     * @param path
     * @param bundle
     * @return
     */
    public static SimpleFragment getFragment(String path, Bundle bundle) {
        return (SimpleFragment) ARouter.getInstance()
                .build(path)
                .with(bundle)
                .navigation();
    }

    /**
     * 根据path返回Activity
     *
     * @param path
     * @return
     */
    public static AbstractSimpleActivity getActivity(String path) {
        return (AbstractSimpleActivity) ARouter.getInstance()
                .build(path)
                .navigation();
    }

    /**
     * 根据path返回Activity
     *
     * @param path
     * @param bundle
     * @return
     */
    public static AbstractSimpleActivity getActivity(String path, Bundle bundle) {
        return (AbstractSimpleActivity) ARouter.getInstance()
                .build(path)
                .with(bundle)
                .navigation();
    }


    /**
     * 带返回参数的跳转activity
     *
     * @param activity
     * @param path
     * @param bundle
     * @param requestCode
     */
    public static void getActivity(Activity activity, String path, Bundle bundle, int requestCode) {
        ARouter.getInstance()
                .build(path)
                .with(bundle)
                .navigation(activity, requestCode);
    }
}
