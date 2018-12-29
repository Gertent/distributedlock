package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author wyf
 * @Date 2018/12/29 10:16
 * @Desc 实现分布式Barrier
 * @Version 1.0
 */
public class Recipes_Barrier {
    private static String barrier_path = "/curator_recipes_barrier_path";
    private static String connectStr = "127.0.0.1:2181";
    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i< 5; i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory
                                .builder()
                                .connectString(connectStr)
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                                .build();
                        client.start();
                        barrier = new DistributedBarrier(client, barrier_path);
                        System.out.println(Thread.currentThread().getName() + "号barrier设置");
                        barrier.setBarrier();
                        barrier.waitOnBarrier();
                        System.out.println("启动");
                    }catch (Exception e){}
                }
            }).start();
        }
        Thread.sleep(50000);
        barrier.removeBarrier();
    }
}
