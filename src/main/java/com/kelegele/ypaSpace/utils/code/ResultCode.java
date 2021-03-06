package com.kelegele.ypaSpace.utils.code;

public enum ResultCode implements IErrorCode {
    SUCCESS(20000, "操作成功"),
    FAILED(50000, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(50014, "token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
