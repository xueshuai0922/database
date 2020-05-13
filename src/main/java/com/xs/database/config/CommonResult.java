package com.xs.database.config;

/**
 * @Author 薛帅
 * @Date 2019/3/29 15:11
 * @Description 返回结果封装类
 */
public class CommonResult<T> {
    private long code;
    private String Message;
    private T data;

    private CommonResult() {
    }

    public  CommonResult(long code, String Message, T data) {
        this.code = code;
        this.Message = Message;
        this.data = data;
    }

    /**
     * 静态方法，成功返回对应的消息代码 消息主体 <T>表示泛型方法
     * @param data
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(T data ){
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 错误返回，参数为错误代码接口
     * @param errorCode 接口
     * @param <T>
     * @return
     */
    public static  <T> CommonResult<T> failed(IErrorCode errorCode){
        return new CommonResult<T>(errorCode.getCode(),errorCode.getMessage(),null);
    }

    /**
     *  错误返回，参数为错误消息
     * @param message
     * @param <T>
     * @return
     */
    public static  <T> CommonResult<T> failed(String message){
        return  new CommonResult<T>(ResultCode.FAILED.getCode(),message,null);
    }

    /**
     * 错误返回，默认的错误信息
     * @param <T>
     * @return
     */
    public  static  <T> CommonResult<T> failed (){
        return new CommonResult<T>( ResultCode.FAILED.getCode(),
                ResultCode.FAILED.getMessage(),null);
    }



    public long getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
