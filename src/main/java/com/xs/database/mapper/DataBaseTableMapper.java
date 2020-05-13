package com.xs.database.mapper;

import com.xs.database.entity.DBTableEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 薛帅
 * @Date 2019/3/25 20:25
 * @Description
 */
@Mapper
public interface DataBaseTableMapper {
    List<DBTableEntity> getTableListByOracle(String tableName);
    List<DBTableEntity> getTableListByMysql(String tableName);

    int testCon();
}
