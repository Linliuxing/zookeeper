package com.lin.zookpeer.nativeAPI;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Title: ZookpeerACLTest
 * @Description: 自定义权限
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/23 17:26
 */
public class ZkACLTest implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(ZkACLTest.class);

    private ZooKeeper zooKeeper = null;

    private static final String zkServerPath = "172.16.1.160:2181";

    private static final Integer timeOut = 5000;

    public ZkACLTest(){

    }

    public ZkACLTest(String connectionString){
        try{
            zooKeeper = new ZooKeeper(connectionString,timeOut,new ZkACLTest());
        }catch (IOException e){
            e.printStackTrace();
            if(zooKeeper != null){
                try{
                    zooKeeper.close();
                }catch (InterruptedException el){
                    el.printStackTrace();
                }
            }
        }
    }

    public void createZKNode(String path,byte[] data,List<ACL> acls){
        String result ="";
        try{
            result = zooKeeper.create(path,data,acls, CreateMode.PERSISTENT);
            logger.info("创建节点：\t"+result+"\t成功...");
        }catch (KeeperException e){
            e.printStackTrace();
        }catch (InterruptedException el){
            el.printStackTrace();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }


    public static void main(String[] args) throws Exception{
        ZkACLTest zkServer = new ZkACLTest(zkServerPath);

        //任何人都能访问
//        zkServer.createZKNode("/aclimooc","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);

        //自定义用户认证访问
        /*
        * [zk: localhost:2181(CONNECTED) 5] getAcl /aclimooc/testdigest
          'digest,'imooc1:ee8R/pr2P4sGnQYNGyw2M5S5IMU=
          : cdrwa
          'digest,'imooc2:eBdFG0gQw0YArfEFDCRP3LzIp6k=
          : r
          'digest,'imooc2:eBdFG0gQw0YArfEFDCRP3LzIp6k=
          : cd

        * */
//        List<ACL> acls = new ArrayList <ACL>();
//        Id imooc1 = new Id("digest",AclUtils.getDigestUserPwd("imooc1:123456"));
//        Id imooc2 = new Id("digest",AclUtils.getDigestUserPwd("imooc2:123456"));
//        acls.add(new ACL(ZooDefs.Perms.ALL,imooc1));
//        acls.add(new ACL(ZooDefs.Perms.READ,imooc2));
//        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE,imooc2));
//        zkServer.createZKNode("/aclimooc/testdigest","testdigest".getBytes(), acls);

        //注册过的用户必须通过addAuthInfo才能操作节点
//        zkServer.getZooKeeper().addAuthInfo("digest","imooc1:123456".getBytes());
//        zkServer.createZKNode("/aclimooc/testdigest/childtest","childtest".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL);
//        Stat stat = new Stat();
//        byte[] data = zkServer.getZooKeeper().getData("/aclimooc/testdigest",false,stat);
//        System.out.println(new String(data));
//        zkServer.getZooKeeper().setData("/aclimooc/testdigest","123456".getBytes(),0);

        //ip方式的acl
//        List<ACL> aclsIP = new ArrayList <ACL>();
//        Id ipId1 = new Id("ip","172.16.0.67");
//        aclsIP.add(new ACL(ZooDefs.Perms.ALL,ipId1));
//        zkServer.createZKNode("/aclimooc/iptest","123456".getBytes(),aclsIP);

        //验证ip是否又权限
//        zkServer.getZooKeeper().setData("/aclimooc/iptest","123456".getBytes(),0);
//        zkServer.getZooKeeper().setData("/aclimooc/iptest","now".getBytes(),1);
        Stat stat = new Stat();
        byte[] data = zkServer.getZooKeeper().getData("/aclimooc/iptest",false,stat);
        System.out.println(new String(data));
        System.out.println(stat.getVersion());

    }





    public void process(WatchedEvent watchedEvent) {

    }
}
