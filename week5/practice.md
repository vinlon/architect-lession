## 作业

1. 用你熟悉的语言实现一致性哈希算法
2. 编写用例测试这个算法，测试100万KV数据 ，10个服务器结点的情况下，这些KV数据在服务器上分布数量的标准差，以评估算法的存储负载不均衡性。


#### 算法类实现

```java
public class HashRouter {
    private Integer virtualNodeCount;
    private SortedMap<Integer, String> circle = new TreeMap<>();

    public HashRouter(Integer virtualNodeCount) {
        this.virtualNodeCount = virtualNodeCount;
    }

    /**
     * 添加节点
     */
    public void addNode(String nodeKey) {
        //将节点拆分为指定数量的虚拟节点
        for (int i = 0; i < virtualNodeCount; i++) {
            String virtualKey = String.format("%s#%s", nodeKey, i);
            //计算虚拟节点的Hash值，并记录对应关系
            circle.put(hash(virtualKey), nodeKey);
        }
    }

    /**
     * 查找一个Key的存储节点
     */
    public String route(String key) {
        //计算Key的哈希值
        Integer hashValue = hash(key);
        //查找距离该值最近的虚拟节点
        SortedMap<Integer, String> subCircle = circle.tailMap(hashValue);
        if (subCircle.isEmpty()) {
            return circle.get(circle.firstKey());
        }
        return subCircle.get(subCircle.firstKey());
    }

    /**
     * 计算哈希值
     */
    private Integer hash(String key) {
        return Math.abs(com.google.common.hash.Hashing.murmur3_32().hashUnencodedChars(key).asInt());
    }
}

```

#### 测试代码

```java
public class HashRouterTest {
    @Test
    public void route() {
        Integer[] virtualNodeCountSet = {10, 100, 1000, 10000, 100000};
        Integer testKeyCount = 100 * 10000;
        Set<String> testKeySet = new HashSet<>(testKeyCount);
        for (int i = 0; i < testKeyCount; i++) {
            testKeySet.add(UUID.randomUUID().toString());
        }
        System.out.println("虚拟节点数量, 标准差");
        for (Integer virtualNodeCount : virtualNodeCountSet) {
            testRoute(testKeySet, virtualNodeCount);
        }
    }

    private void testRoute(Collection<String> testKeySet, Integer virtualNodeCount) {
        SortedMap<String, Integer> stat = new TreeMap<String, Integer>();
        Map<String, String> routeResult = new HashMap<>(testKeySet.size());
        HashRouter router = new HashRouter(virtualNodeCount);
        for (int i = 0; i < 10; i++) {
            router.addNode(String.format("node%s", i));
        }
        for (String key : testKeySet) {
            String target = router.route(key);
            //记录K的分布情况
            stat.put(target, stat.getOrDefault(target, 0) + 1);
            routeResult.put(key, target);
        }
        System.out.printf("%s,  %s \n", virtualNodeCount, calculateSD(stat.values()));
    }

     /**
     * 计算标准差
     */
    private double calculateSD(Collection<Integer> numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();
        for (Integer num : numArray) {
            sum += num;
        }
        double mean = sum / length;
        for (Integer num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation / length);
    }
}
```

测试运行结果 

```
虚拟节点数量, 标准差
10,  38563.063560873896 
100,  9300.812652666433 
1000,  2036.2706106998646 
10000,  1557.482519966115 
100000,  398.58248832581694 
```