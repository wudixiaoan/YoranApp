package com.ph.common.base;

public interface BaseView {
    /**
     * 页面状态
     */
    static final int STATE_NORMAL = 0x00;
    static final int STATE_LOADING = 0x01;
    static final int STATE_ERROR = 0x02;

    /**
     * 正常显示
     */
    void showNormal();

    /**
     * 显示错误
     */
    void showError();

    /**
     * 正在加载
     */
    void showLoading();

    /**
     * 显示错误信息
     * @param errorMsg 错误信息
     */
    void showErrorMsg(String errorMsg);
}
