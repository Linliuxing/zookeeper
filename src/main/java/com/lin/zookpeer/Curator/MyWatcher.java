package com.lin.zookpeer.Curator;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Title: MyWatcher
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/24 15:20
 */
public class MyWatcher implements Watcher{
    public void process(WatchedEvent watchedEvent) {
        System.out.println("出发watcher，节点路径为："+watchedEvent.getPath());
    }
}
