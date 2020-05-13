package com;

import com.xs.database.entity.Demo;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.tran.DataStream;
import org.frameworkset.tran.db.input.es.DB2ESImportBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class DatabaseApplicationTests {
    @Test
    public void startEs() {
        String relativePath =
                ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(relativePath);
        String cmd = "C:/Git/database/src/main/resources/elasticsearch-7.3.2/bin/elasticsearch.bat";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String testEs = "curl localhost:9200";
        try {
            Runtime.getRuntime().exec(testEs);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSimpleImportBuilder() {
        DB2ESImportBuilder importBuilder = DB2ESImportBuilder.newInstance();
        try {
            //清除测试表数据
            ElasticSearchHelper.getRestClientUtil().dropIndice("dbclobdemo");
        } catch (Exception e) {

        }
        //数据源相关配置，可选项，可以在外部启动数据源
        importBuilder.setDbName("test")
                .setDbDriver("oracle.jdbc.driver.OracleDriver") //数据库驱动程序，必须导入相关数据库的驱动jar包
                .setDbUrl("jdbc:oracle:thin:@123.57.224.129:1521:develop") //通过useCursorFetch=true启用mysql的游标fetch机制，否则会有严重的性能隐患，useCursorFetch必须和jdbcFetchSize参数配合使用，否则不会生效
                .setDbUser("hrpdev")
                .setDbPassword("hrpdev_2019")
                .setValidateSQL("select 1")
                .setUsePool(false);//是否使用连接池
        long sTime = System.currentTimeMillis();
        System.out.println("导入数据开始");
        //指定导入数据的sql语句，必填项，可以设置自己的提取逻辑
        importBuilder.setSql("select s.TABLE_NAME ,\n" +
                        "         t.comments as table_name_cn,\n" +
                        "         s.COLUMN_NAME,\n" +
                        "         c.comments as column_name_cn,\n" +
                        "         s.DATA_TYPE,\n" +
                        "         s.DATA_DEFAULT,--long\n" +
                        "         s.NULLABLE,\n" +
                        "         s.DATA_LENGTH --number\n" +
                        "    from user_tab_columns S\n" +
                        "    inner join USER_TAB_COMMENTS t\n" +
                        "      on t.table_name = s.TABLE_NAME\n" +
                        "      and t.table_type = 'TABLE'\n" +
                        "      left join user_col_comments c\n" +
                        "      on c.table_name = s.TABLE_NAME\n" +
                        "      and c.column_name = s.COLUMN_NAME\n"

        );
        /**
         * es相关配置
         */
        importBuilder
                .setIndex("dbclobdemo") //必填项
                .setIndexType("dbclobdemo") //es 7以后的版本不需要设置indexType，es7以前的版本必需设置indexType
                .setRefreshOption(null)//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");表示实时刷新
                .setUseJavaName(true) //可选项,将数据库字段名称转换为java驼峰规范的名称，例如:doc_id -> docId
                .setBatchSize(5000)  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理
                .setJdbcFetchSize(10000);//设置数据库的查询fetchsize，同时在mysql url上设置useCursorFetch=true启用mysql的游标fetch机制，否则会有严重的性能隐患，jdbcFetchSize必须和useCursorFetch参数配合使用，否则不会生效


        importBuilder.setParallel(true);//设置为多线程并行批量导入
        importBuilder.setQueue(100);//设置批量导入线程池等待队列长度
        importBuilder.setThreadCount(200);//设置批量导入线程池工作线程数量
        importBuilder.setContinueOnError(true);//任务出现异常，是否继续执行作业：true（默认值）继续执行 false 中断作业执行
        importBuilder.setAsyn(false);//true 异步方式执行，不等待所有导入作业任务结束，方法快速返回；false（默认值） 同步方式执行，等待所有导入作业任务结束，所有作业结束后方法才返回
        importBuilder.setRefreshOption("refresh"); // 为了实时验证数据导入的效果，强制刷新数据，生产环境请设置为null或者不指定


        /**
         * 执行数据库表数据导入es操作
         */
        DataStream dataStream = importBuilder.builder();
        System.out.println("下一步就是执行。。。。");
        dataStream.execute();
        long eTime = System.currentTimeMillis();
        System.out.println("导入结束,消耗时间："+(eTime-sTime)/1000+"s");//消耗时间和正常的jdbc
        dataStream.destroy();//执行完毕后释放资源
    }
    @Test
    public  void  getEsResult(){

    }
    @Autowired
    private BBossESStarter bbossESStarter;
    @Test
    public void testBulkAddDocuments() {
        //创建批量创建文档的客户端对象，单实例多线程安全
        ClientInterface clientUtil = bbossESStarter.getRestClient();
        List<Demo> demos = new ArrayList<Demo>();
        Demo demo = null;

        for(int i = 0 ; i < 200002; i ++) {
            demo = new Demo();//定义第一个对象
            demo.setDemoId((long)i);
            demo.setAgentStarttime(new Date());
            demo.setApplicationName("blackcatdemo"+i);
            demo.setContentbody("this is content body"+i);
            if(i % 2 == 0) {
                demo.setName("刘德华喜欢唱歌" + i);
            }
            else{
                demo.setName("张学友不喜欢唱歌" + i);
            }
            demos.add(demo);//添加第一个对象到list中
        }

        long start = System.currentTimeMillis();
        //批量添加或者修改文档，将两个对象添加到索引表demo中
        String response = clientUtil.addDocuments("demo",//索引表
                "demo",//索引类型
                demos,"refresh=true");//为了测试效果,启用强制刷新机制
        long end = System.currentTimeMillis();
        System.out.println("BulkAdd 20002 Documents elapsed:"+(end - start)+"毫秒");
        start = System.currentTimeMillis();
        long count = clientUtil.countAll("demo");
        end = System.currentTimeMillis();
        System.out.println("countAll Documents elapsed:"+(end - start)+"毫秒");
        System.out.println("addDocuments-------------------------" +count);
        System.out.println(response);
        //获取第一个文档.
        start = System.currentTimeMillis();
        response = clientUtil.getDocument("demo",//索引表
                "demo",//索引类型
                "2");//w
        end = System.currentTimeMillis();
        System.out.println("getDocument Json elapsed:"+(end - start)+"毫秒");
        System.out.println("getDocument-------------------------");
        System.out.println(response);
        //获取第二个文档
        start = System.currentTimeMillis();
        demo = clientUtil.getDocument("demo",//索引表
                "demo",//索引类型
                "3",//文档id
                Demo.class);
        end = System.currentTimeMillis();
        System.out.println("getDocument Object elapsed:"+(end - start)+"毫秒");
    }



}


