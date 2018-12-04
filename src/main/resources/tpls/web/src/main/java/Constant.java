package ${groupId}.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Descripton：静态常量类
 * Author: tanggang
 * Date: 2018/11/16
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class Constant {
    //业务处理thread pool
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
}
