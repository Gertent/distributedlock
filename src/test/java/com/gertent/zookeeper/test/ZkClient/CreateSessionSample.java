package com.gertent.zookeeper.test.ZkClient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @Author wyf
 * @Date 2017/12/28 16:20
 * @Desc
 * @Version 1.0
 */
public class CreateSessionSample {

    public static void main(String[] args) {
        String connectStr = "127.0.0.1:2181";
        ZkClient zkClient = new ZkClient(connectStr, 5000);
        System.out.println("创建会话成功");
        String path = "/c1";
        zkClient.createPersistent(path);
    }
}
