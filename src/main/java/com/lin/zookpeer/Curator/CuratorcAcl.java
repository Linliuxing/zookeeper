package com.lin.zookpeer.Curator;

import com.lin.zookpeer.utils.AclUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: CuratorcAcl
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/24 18:09
 */
public class CuratorcAcl {

    public CuratorFramework client = null;

    private static final String zkServerPath = "172.16.1.160:2181";

    public CuratorcAcl(){
        RetryPolicy retryPolicy =new RetryNTimes(3,500);
        client = CuratorFrameworkFactory.builder()
                .authorization("digest","imooc1:123456".getBytes())
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();

    }

    public void closeZKClient(){
        if(client != null){
            client.close();
        }
    }


    public static void main(String[] args) throws Exception{
        CuratorcAcl cto = new CuratorcAcl();
        boolean isZKStarted = cto.client.isStarted();
        System.out.println("当前客户端的状态："+(isZKStarted ? "连接中":"已关闭"));

        //创建节点
        final String nodePath = "/acl/imooc";

        List<ACL> acls = new ArrayList<ACL>();
        Id imooc1 = new Id("digest", AclUtils.getDigestUserPwd("imooc1:123456"));
        Id imooc2 = new Id("digest",AclUtils.getDigestUserPwd("imooc2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL,imooc1));
        acls.add(new ACL(ZooDefs.Perms.READ,imooc2));
        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE,imooc2));

        //创建节点
//        byte[] data = "spiderman".getBytes();
//        cto.client.create().creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(acls)
//                .forPath(nodePath,data);


        cto.client.setACL().withACL(acls).forPath("/curatorNode");

        //更新节点数据
//        byte[] newData = "123456".getBytes();
//        cto.client.setData().withVersion(0).forPath(nodePath,newData);

        //删除节点
//        cto.client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(0).forPath(nodePath);

        //读取节点
//        Stat stat = new Stat();
//        byte[] data = cto.client.getData().storingStatIn(stat).forPath(nodePath);
//        System.out.println("节点"+nodePath+"的数据为："+ new String(data));
//        System.out.println("该节点的版本号为："+stat.getVersion());

        Thread.sleep(1000);

        //关闭客户端
        cto.closeZKClient();

        boolean isZKStarted2 = cto.client.isStarted();
        System.out.println("当前客户端的状态："+(isZKStarted2 ? "连接中":"已关闭"));

    }

}
