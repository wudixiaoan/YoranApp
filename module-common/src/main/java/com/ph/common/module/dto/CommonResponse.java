package com.ph.common.module.dto;

public class CommonResponse<T> {

    /**
     * return_code : 666
     * return_msg : success
     * result_code : 666
     * result_msg : [{"requestSerial":"Sort20200930001417485","state":0},{"requestSerial":"Sort20200930001528840","state":1},{"requestSerial":"Sort20200930001558577","state":2}]
     */

    private int return_code;
    private String return_msg;
    private int result_code;
    private T result_msg;
    /**
     * err_code : SIGN_ERROR
     * err_code_desc : 签名错误
     */

    private String err_code;
    private String err_code_desc;

    public int getReturn_code() {
        return return_code;
    }

    public void setReturn_code(int return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public T getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(T result_msg) {
        this.result_msg = result_msg;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_desc() {
        return err_code_desc;
    }

    public void setErr_code_desc(String err_code_desc) {
        this.err_code_desc = err_code_desc;
    }
}

