package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @Author wyf
 * @Date 2017/12/29 9:30
 * @Desc 分布式锁
 * @Version 1.0
 */
public class Recipes_Lock {
    private static String connectStr = "127.0.0.1:2181";
    private static String lock_path = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
        final CountDownLatch latch = new CountDownLatch(1);
        for(int i = 0;i < 30; i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        latch.await();
                        lock.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的单号为：" + orderNo);
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch.countDown();
        System.out.println("全部完成！！！");
    }
}
