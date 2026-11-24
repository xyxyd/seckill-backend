package com.seckill.common;

/**
 * 业务异常：message 会直接返回给前端
 */
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String message) {
        this(1, message);
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
