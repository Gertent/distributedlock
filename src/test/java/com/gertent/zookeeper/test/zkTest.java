package com.gertent.zookeeper.test;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @Description
 * @Author wyf
 * @Date 2017/12/7
 */
public class zkTest implements Watcher{
    public static void main(String[] args) {
//        try {
//            zkTest watcher =new zkTest();
//            ZooKeeper zk = new ZooKeeper("127.0.0.1:2182",3000,watcher);
//            System.out.println(zk.getState());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String strs="a,b,c";
        String[] str={};
        str=strs.split(",");
        System.out.println(str.length+","+str[0]);

    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.getPath());
    }
}
