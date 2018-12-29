package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author wyf
 * @Date 2018/12/28 17:18
 * @Desc Master选举
 * @Version 1.0
 */
public class Recipes_MasterSelect {
    private static String connectStr = "127.0.0.1:2181";
    private static String master_path = "/curator_recipes_master_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(connectStr)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws InterruptedException {
        client.start();
        LeaderSelector selector = new LeaderSelector(client,
                master_path,
                new LeaderSelectorListenerAdapter() {
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.out.println("成为Master角色");
                        Thread.sleep(3000);
                        System.out.println("完成Master操作，释放Master权力");
                    }
                });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
