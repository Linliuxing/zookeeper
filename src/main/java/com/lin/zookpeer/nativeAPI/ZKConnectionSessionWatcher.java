package com.lin.zookpeer.nativeAPI;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: ZKConnectionSessionWatcher
 * @Description: 会话重连
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/23 16:00
 */
public class ZKConnectionSessionWatcher implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(ZKConnectionSessionWatcher.class);

    private static final String zkServerPath = "172.16.1.160:2181";

    private static final Integer timeOut = 5000;

    public static void main(String[] args) throws Exception{
        ZooKeeper zk = new ZooKeeper(zkServerPath,timeOut,new ZKConnectionSessionWatcher());

        long sessionId = zk.getSessionId();
        byte[] sessionPassword = zk.getSessionPasswd();

        logger.info("客户端开始连接Zookeeper服务器...");
        logger.info("客户端状态:{}",zk.getState());
        new Thread().sleep(200);
        logger.info("客户端状态:{}",zk.getState());

        logger.info("开始会话重连....");
        ZooKeeper zkSession = new ZooKeeper(zkServerPath,timeOut,new ZKConnectionSessionWatcher(),sessionId,sessionPassword);
        logger.info("客户端开始连接Zookeeper服务器...");
        logger.info("客户端状态:{}",zkSession.getState());
        new Thread().sleep(200);
        logger.info("客户端状态:{}",zkSession.getState());


    }

    public void process(WatchedEvent watchedEvent) {


    }
}
