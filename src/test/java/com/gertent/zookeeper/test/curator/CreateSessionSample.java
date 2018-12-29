package com.gertent.zookeeper.test.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Author wyf
 * @Date 2018/12/28 16:39
 * @Desc
 * @Version 1.0
 */
public class CreateSessionSample {

    public static void main(String[] args) throws Exception {
        String connectStr = "127.0.0.1:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectStr, 5000, 3000, retryPolicy);
        client.start();
        System.out.println("会话创建成功");


        //Fluent风格Api创建ZK客户端
        CuratorFramework client2 = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client2.start();
        client2.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/zkaa","init".getBytes());
    }
}
