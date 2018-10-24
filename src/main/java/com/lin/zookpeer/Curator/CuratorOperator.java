package com.lin.zookpeer.Curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Title: CuratorOperator
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/24 13:41
 */
public class CuratorOperator {

    private static Logger logger = LoggerFactory.getLogger(CuratorOperator.class);

    public CuratorFramework client = null;

    private static final String zkServerPath = "172.16.1.160:2181";

    public CuratorOperator(){
        /*
        *
        * Curator连接Zookeeper的策略：ExponentialBackoffRetry
        * baseSleepTimeMs：初始sleep的时间
        * maxRetries：最大重试次数
        * maxSleepMs：最大重试时间
        *
        * */
//        RetryPolicy retryPolicy1 = new ExponentialBackoffRetry(1000,5);

        /*
        *
        * Curator连接Zookeeper的策略：RetryNTimes
        * n:重试次数
        * sleepMsBetweenRetries:每次重试间隔的时间
        * */
        RetryPolicy retryPolicy2 =new RetryNTimes(3,500);


        /*
        *
        * Curator连接Zookeeper的策略：retryPolicy
        * sleepMsBetweenRetry:每次重试间隔的时间
        * */
//        RetryPolicy retryPolicy3 = new RetryOneTime(500);

        /*
        * Curator连接Zookeeper的策略：RetryForever
        * 永远重试，不推荐使用
        * */
//        RetryPolicy retryPolicy4 = new RetryForever(retryIntervalMs);

        /*
        *
        * Curator连接Zookeeper的策略：RetryUntilElapsed
        * maxElapsedTimeMs:最大重试时间
        * sleepMsBetweenRetries:每次重试间隔的时间
        * 重试时间超过maxElapsedTimeMs后，不在重试
        * */
        RetryPolicy retryPolicy5 = new RetryUntilElapsed(2000,3000);

        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy2)
                .namespace("workspace").build();
        client.start();

    }


    public void closeZKClient(){
        if(client != null){
            client.close();
        }
    }

    public static void main(String[] args) throws Exception{
        //实例化
        CuratorOperator cto = new CuratorOperator();
        boolean isZKStarted = cto.client.isStarted();
        logger.info("当前客户端的状态："+(isZKStarted ? "连接中":"已关闭"));

        //创建节点
        final String nodePath = "/super/imooc";
//        byte[] data = "super".getBytes();
//        cto.client.create().creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//                .forPath(nodePath,data);

        //更新节点数据
//        byte[] newdata = "new123".getBytes();
//        cto.client.setData().withVersion(0).forPath(nodePath,newdata);

        //删除节点
//        cto.client.delete()
//                .guaranteed() //如果删除失败，那么在后端还是会继续删除，知道成功
//                .deletingChildrenIfNeeded() //如果有子节点，就删除
//                .withVersion(1)
//              forPath(nodePath);

        //读取节点信息
//        Stat stat = new Stat();
//        byte[] data = cto.client.getData().storingStatIn(stat).forPath(nodePath);
//        System.out.println("节点"+nodePath+"的数据为："+ new String(data));
//        System.out.println("该节点的版本号为："+stat.getVersion());

        //查询子节点
//        List<String> childNodes = cto.client.getChildren().forPath(nodePath);
//        System.out.println("开始打印子节点");
//        for (String s :childNodes){
//            System.out.println(s);
//        }

        //判断节点是否存在，如果不存在则为空
//        Stat statExist = cto.client.checkExists().forPath(nodePath);
//        System.out.println(statExist);

        //watcher 事件 当使用usingWatcher的时候，监听只会触发一次，监听完毕后就销毁
//        cto.client.getData().usingWatcher(new MyCuratorWatcher()).forPath(nodePath);
//        cto.client.getData().usingWtcher(new MyWatcher()).forPath(nodePath);


        /*Curator之nodeCache一次注册，N次监听*/
        //为节点添加watcher
        //监听数据节点的变更，会触发事件
        final NodeCache nodeCache = new NodeCache(cto.client,nodePath);
        //buildInitial: 初始化的时候获取node的值并且缓存
        nodeCache.start(true);
        if(nodeCache.getCurrentData() != null){
            System.out.println("节点的初始化数据为："+new String(nodeCache.getCurrentData().getData()));
        }else{
            System.out.println("节点初始化数据为空。。。");
        }

        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                //获取当前数据
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("节点路径为："+nodeCache.getCurrentData().getPath()+" 数据: "+data);
            }
        });


        Thread.sleep(100000);

        //关闭客户端
        cto.closeZKClient();

        boolean isZKStarted2 = cto.client.isStarted();
        logger.info("当前客户端的状态："+(isZKStarted2 ? "连接中":"已关闭"));

    }
}
