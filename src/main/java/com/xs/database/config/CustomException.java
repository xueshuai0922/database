package com.xs.database.config;

/**
 * @Author 薛帅
 * @Date 2019/3/29 15:09
 * @Description 自定义异常类
 */
public class CustomException extends  RuntimeException{
    private long  errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        return "CustomException{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
    private CustomException(){

    }
    public  CustomException(long  errorCode,String errorMsg){
        this.errorCode=errorCode;
        this.errorMsg= errorMsg;
    }

    public CustomException(ResultCode resultCode){
        this.errorCode=resultCode.code;
        this.errorMsg= resultCode.message;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
