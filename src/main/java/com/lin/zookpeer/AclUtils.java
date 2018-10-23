package com.lin.zookpeer;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * @Title: AclUtils
 * @Description: acl加密工具类
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/23 17:34
 */
public class AclUtils {

    public static String getDigestUserPwd(String id) throws Exception{
        return DigestAuthenticationProvider.generateDigest(id);
    }

    public static void main(String[] args) throws Exception {
        String id = "imooc:imooc";
        String pwd = getDigestUserPwd(id);
        System.out.println(pwd);
    }
}
