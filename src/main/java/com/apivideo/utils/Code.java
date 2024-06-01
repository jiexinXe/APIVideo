package com.apivideo.utils;



public enum Code {

    rc200(200,"访问成功"),
    rc400(400,"访问失败"),
    rc403(403,"权限不足，不可访问");

    private Integer code;
    private String msg;

    Code(Integer code,String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

}
