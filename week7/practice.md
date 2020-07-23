## 性能压测的时候，随着并发压力的增加，系统响应时间和吞吐量如何变化，为什么？

随着并发数量的不断增加：  
系统响应时间在前期会基本保持一致，而在并发数超过某个点后，响应时间会慢慢增加(我们称这个点为『系统最佳运行点』)
吞吐量会平衡增长，在并发数超过『系统最佳运行点后』，增长越来越慢，直到超过某个点后，吞吐量开始下降（我们称这个点为『系统最大负载点』)

出现上述规律的原因是： 在服务器资源没有完全利用的情况下，系统响应时间是由代码的执行效率决定的，但服务器的资源是有限的，随着并发数越来越高，应用服务器或数据库服务器会慢慢到达瓶颈，处理时间变长，导致系统响应时间下降。


## 用你熟悉的编程语言写一个 web 性能压测工具，输入参数：URL，请求总次数，并发数。输出参数：平均响应时间，95% 响应时间。用这个测试工具以 10 并发、100 次请求压测 www.baidu.com

注： 此版本代码运行过程中还存在一些问题，由于对java并发编程不是很熟悉，暂未解决。 

```java
@Slf4j
public class ConcurrentTest {
    private double getPercentile(long[] array, double percentile) {
        Arrays.sort(array);
        double x = (array.length - 1) * percentile;
        int i = (int) x;
        double j = x - i;
        return (1 - j) * array[i] + j * array[i + 1];
    }

    @Test
    public void test() throws InterruptedException {
        //测试地址: https://www.geekbang.org
        String url = "https://www.geekbang.org";
        //并发数: 10, 请求次数: 100
        concurrentRun(10, 100, url);
    }

    private void concurrentRun(int threadCount, int requestCount, String url) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(requestCount);
        long[] costs = new long[requestCount];
        for (int i = 0; i < requestCount; i++) {
            int loop = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    long cost = callUrl(url);
                    costs[loop] = cost;
                    semaphore.release();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("\n" +
                "请求地址:" + url + "\n" +
                "并发数:" + threadCount + "\n" +
                "请求次数:" + requestCount + "\n" +
                "平均响应时间:" + LongStream.of(costs).average().getAsDouble() + " ms \n" +
                "95%响应时间:" + getPercentile(costs, 0.95) + " ms \n" +
                "\n");
    }

    private long callUrl(String urlStr) throws IOException {
        long start = System.currentTimeMillis();
        URL url = new URL(urlStr + "?t=" + Math.random());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.disconnect();
        long end = System.currentTimeMillis();
        long cost = end - start;
        log.info("cost:{}.", cost);
        return cost;
    }
}
```