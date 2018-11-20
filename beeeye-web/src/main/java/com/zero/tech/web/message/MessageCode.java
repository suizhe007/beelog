package com.zero.tech.web.message;

/**
 * @desc 消息码
 */
public enum MessageCode {

    SUCCESS("0000", "提交成功"),
    FAILED("0001", "提交失败");

    private String code;
    private String msg;

    MessageCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
