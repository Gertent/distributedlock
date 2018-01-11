package com.gertent.zookeeper.test;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * @Description
 * @Author wyf
 * @Date 2017/12/7
 */
public class Master implements Watcher {
    private static Logger logger = Logger.getLogger(Master.class);
    ZooKeeper zk;
    String hostPort;
    Master(String hostPort) {
        this.hostPort = hostPort;
    }
    void startZK() {
        try {
            zk = new ZooKeeper(hostPort, 15000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent e) {
        logger.debug(e);
        System.out.println(e);
    }
    public void stopZK(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    String serverId = Integer.toString(new Random(100L).nextInt());
    boolean isLeader = false;

    boolean checkMaster() {
        while (true) {
            try {
                Stat stat = new Stat();
                byte data[] = zk.getData("/master", false, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (KeeperException.NoNodeException e) {
// no master, so try create again
                return false;
            } catch (KeeperException.ConnectionLossException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }
    public void runForMaster() throws InterruptedException {
        while (true) {try {
            zk.create("/master", serverId.getBytes(),
                    OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            isLeader = true;
            break;
        } catch (KeeperException.NodeExistsException e) {
            isLeader = false;
            break;
        } catch (KeeperException.ConnectionLossException e) {
        } catch (KeeperException e) {
            e.printStackTrace();
        }
            if (checkMaster()) break;
        }
    }
    public static void main(String args[])
            throws Exception {
        Master m = new Master("127.0.0.1:2182");
        m.startZK();
        m.runForMaster();
        boolean isLeader =m.checkMaster();
        if (isLeader) {
            System.out.println("I'm the leader");// wait for a bit
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is the leader");
        }
        m.stopZK();
    }

}
