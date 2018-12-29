package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * @Author wyf
 * @Date 2018/12/29 9:46
 * @Desc 分布式计数器
 * @Version 1.0
 */
public class Recipes_DistAtomicInt {

    private static String connectStr = "127.0.0.1:2181";
    private static String distatomicint_path = "/curator_recipes_distatomicint_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, distatomicint_path, new RetryNTimes(3, 1000));
        for(int i = 0;i<10;i++) {
            AtomicValue<Integer> rc = atomicInteger.add(8);
            System.out.println("Result:" + rc.succeeded() + "," + rc.postValue());
        }
    }
}
