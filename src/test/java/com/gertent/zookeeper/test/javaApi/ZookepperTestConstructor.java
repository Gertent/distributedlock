package com.gertent.zookeeper.test.javaApi;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author wyf
 * @Date 2017/12/28 14:54
 * @Desc java API 连接ZK服务器
 * @Version 1.0
 */
public class ZookepperTestConstructor implements Watcher{

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String connectStr = "127.0.0.1:2181";
        ZooKeeper zooKeeper = new ZooKeeper(connectStr, 5000, new ZookepperTestConstructor());
        System.out.println(zooKeeper.getState());

        String path1 = zooKeeper.create("/zk-test","testData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建节点成功：" + path1);

        String path2 = zooKeeper.create("/zk-test-seq","testData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("创建节点成功：" + path2);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zookeeper会话建立！");
    }

    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()){
            latch.countDown();
        }
    }
}
