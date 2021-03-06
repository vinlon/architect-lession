## 简述JVM垃圾回收原理

垃圾回收即将JVM堆中不再使用对象清理掉，释放内存资源。

首先在整体上使用分代回收机制，将内存分为新生代和老年代两个区域，新生代存储生存周期较短的对象，内存空间较小，回收频率较高。老年代存储生存周期较长的对象，空间较大，回收频率较低。

垃圾回收的处理流程如下：
1. 使用可达性分析算法标记出内存中的『可回收的对象』
2. 使用垃圾回收器算法将标记出的『可回收的对象』进行清理压缩或复制，释放内存空间，并将小块的可用空间合并为连续的可用内存空间。


## 设计一个秒杀系统，主要的挑战和问题有哪些？核心的架构方案或者思路有哪些？

### 秒杀系统的挑战

- 秒杀活动的并发访问量是平时的数百甚至上千倍  

  ```
  高并发下的风险：
  1. 网络带宽耗尽
  2. 服务器负载过高，停止响应
  3. 数据库瘫痪
  ```

- 可能对现有业务造成冲击。

- 秒杀商品库存很少，想要购买的人很多, 防止并发请求的情况下出现负库存。

- 秒杀活动常常是定时开始，且涉及利益较大，需要防作弊。  

  ```
  1. 使用脚本自动刷新页面，活动开始后自动下单。
  2. 跳转秒杀商品页面，直接进入下单页面。
  ```

  

### 秒杀系统架构

- 商品页面静态化

  ```
  活动开始前用户会不停刷新页面，以免错过开始时间，静态页面可以极大减轻数据库压力。
  ```

- 使用CDN对静态资源（图片,静态页面等）进行缓存

  ```
  减少服务器资源及带宽消耗, 加快页面响应速度。
  注：即使是使用CDN，也需要准备远大于平时使用的带宽资源。
  ```

- 独立开发，独立部署，尽量减少和现有系统的耦合度。保证正常业务不会受秒杀活动影响

- 动态生成下单页面/接口URL地址

  ```
  使用随机URL，防止有人路过商品页面直接提交订单。
  ```

- 设置阀门，只放最前面的一部分人进入秒杀系统。



### 页面规划

- 商品页面

  ```
  1. 所有用户都能访问的页面，也是访问压力最大的页面。
  2. 活动开始前生成静态页面，并使用CDN对静态文件进行缓存。
  3. 静态页面上包含一个JS脚本，异步请求服务器，判断秒杀活动是否开始，如果开始则返回动态的下单页面URL.
  ```

  

- 下单页面

  ```
  1. 根据秒杀商品数量，通过计数器，只有前N个用户可以进入下单页面
  ```

  

- 支付页面

  ```
  根据商品的库存，只有在库存归零前下单的用户可以进入支付页面。
  ```

  

- 秒杀结束页面

  ```
  除了前N个用户可以进入下单页面，其它用户直接进入秒杀结束页面。
  即使系统出了故障，也不能显示出错页面而是秒杀结束页面。
  ```

  



### 其它事项

- 性能测试

  ```
  通过性能，负载，压力测试，评估当前的部署方案能否支持预估的并发数，以及最大可以支持的并发数。
  ```