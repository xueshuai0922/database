package com.xs.database.config;

/**
 * @Author 薛帅
 * @Date 2019/4/3 16:14
 * @Description 结果返回代码枚举类
 */
public enum ResultCode implements  IErrorCode{
    SUCCESS(200,"导出成功！"),
    FAILED(500,"导出失败"),
    PRE_FAIL(501,"关闭预编译连接异常"),
    RS_FAIL(502,"关闭结果集异常"),
    CN_FAIL(503,"关闭连接异常"),
    OCN_FAIL(500,"创建连接失败"),
    DRIVER_FAIL(504,"加载驱动失败"),
    ENCODE_FAIL(505,"转码失败"),
    DEFAULT_FAIL(506,"获取数据库表结构失败"),
    SQL_FAIL(506,"查询表字段错误"),
    TASK_FAIL(506,"多线程执行更新表字段失败");



    long code;
    String message;

    public long getCode() {
        return code;
    }

       public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
