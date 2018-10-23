package com.lin.zookpeer;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;


/**
 * @Title: Zkclient
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/7/2 14:48
 */
public class Zkclient {
    public static void main(String[] args) throws Exception{
        ZkClient zkClient = new ZkClient("192.168.0.183:2181");//建立连接
        zkClient.create("/root","mydata", CreateMode.PERSISTENT);//创建目录并写入数据
        String data=zkClient.readData("/root");
        System.out.println(data);
        //zkClient.delete("/root");//删除目录
        //zkClient.deleteRecursive("/root");//递归删除节目录
    }
}
