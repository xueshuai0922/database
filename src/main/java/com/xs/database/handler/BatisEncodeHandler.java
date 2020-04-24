package com.xs.database.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author 薛帅
 * @Date 2020/4/22 15:18
 * @Description  中文乱码处理handler，实现mybatis的typeHandler接口
 *  数据库编码：
 *      解数据库编码                      重新编码
 *      ISO-8559-1                        GBK
 *      GBK/GB2312
 *      unicode UTF-8/UTF-16
 *
 */
@Component
public class BatisEncodeHandler implements TypeHandler<String> {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BatisEncodeHandler(String code) {
        this.code = code;
    }

    public BatisEncodeHandler() {
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {

    }

    /**
     * 进行重新编码
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        try {
            if(string!=null && code != null){
                if("UTF-8".equals(this.code)){
                    return  string;
                }else{
                    byte[] bytes = string.getBytes(this.code);
                    return new String(bytes,"GBK");
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       return string;
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        return string;
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
