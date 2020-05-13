package com.xs.database.serivce;

import com.xs.database.entity.DBTableEntity;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 薛帅
 * @Date 2019/3/26 21:30
 * @Description
 */
public class ResultHandlerImp implements ResultHandler<DBTableEntity> {

    final List list = new ArrayList<DBTableEntity>();
    final int  attch_size = 1000;
    int size = 0;
    @Override
    public void handleResult(ResultContext<? extends DBTableEntity> resultContext) {

        list.add(resultContext.getResultObject());
        size++;
        if(attch_size==size){
            handle();
        }

    }

    private void handle() {
        try {
            // 在这里可以对你获取到的批量结果数据进行需要的业务处理
        } finally {
            // 处理完每批数据后后将临时清空
            size = 0;
            list.clear();
        }
    }

    // 这个方法给外面调用，用来完成最后一批数据处理
    public void end(){
        handle();// 处理最后一批不到BATCH_SIZE的数据
    }
    // 这个方法给外面调用，用来完成最后一批数据处理
    public List getList(){
       return list;
    }
}
