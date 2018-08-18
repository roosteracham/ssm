package com.zsf.util.errorcode;

public enum ErrorCodeEnum {
    SUC("成功", 0),
    FAIL("失败", 1),
    USERNAME_EXIST("用户名已存在", 2),
    USER_EMAIL_EXIST("用户名已存在", 3);
    private String error;

    private int index;

    public String getError() {
        return error;
    }

    ErrorCodeEnum(String error, int index) {
        this.error = error;
        this.index = index;
    }

    public static String getError(int index) {
        for (ErrorCodeEnum c : ErrorCodeEnum.values()) {
            if (c.getIndex() == index) {
                return c.error;
            }
        }
        return null;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
