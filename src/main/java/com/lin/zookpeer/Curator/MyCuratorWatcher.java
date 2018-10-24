package com.lin.zookpeer.Curator;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * @Title: MyCuratorWatcher
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/24 15:24
 */
public class MyCuratorWatcher implements CuratorWatcher {
    public void process(WatchedEvent watchedEvent) throws Exception {
        System.out.println("触发watcher，节点路径为："+watchedEvent.getPath());
    }
}
