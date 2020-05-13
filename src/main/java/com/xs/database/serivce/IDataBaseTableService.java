package com.xs.database.serivce;

import com.xs.database.entity.ConnectionEntity;

import java.util.List;

/**
 * @Author 薛帅
 * @Date 2019/3/29 14:10
 * @Description
 */

public interface IDataBaseTableService  {
    List getTableList(ConnectionEntity conEntity) throws  Exception;

    boolean testCon(ConnectionEntity conEntity);
}
