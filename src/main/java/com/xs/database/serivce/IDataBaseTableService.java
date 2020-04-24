package com.xs.database.serivce;

import com.xs.database.entity.ConnectionEntity;
import com.xs.database.entity.DBTableEntity;

import java.util.List;

/**
 * @Author 薛帅
 * @Date 2020/3/29 14:10
 * @Description
 */

public interface IDataBaseTableService  {
    List getTableList(ConnectionEntity conEntity) throws  Exception;

    boolean testCon(ConnectionEntity conEntity);
}
