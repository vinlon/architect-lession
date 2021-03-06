## 一个典型的大型互联网应用系统使用了哪些技术方案和手段，主要解决什么问题？请列举描述

- 业务分割

目前典型的手段是微服务化，将业务拆分为多个模块进行开发，一方面方便多个团队进行协作开发，另一方面方便以为单模块位进行性能优化及功能迭代，而不会影响到系统其它系统的运行。

- 分布式

分布式部署：每个系统模块/服务单独部署到一台服务器（这里的服务器可以是一台物理机，也可以是一台虚拟机），目的是保证各个系统模块/服务间的资源隔离，互不影响。 

分布式文件系统和数据库： 方便通过横向扩展的方式提升文件读取及数据库访问的效率。

- 集群

每一个服务都至少部署两个结点，一方面通过横向扩展的方式增强系统的处理能力，另一方面保证系统的可用性，任何一个结点出了问题，都有其它的服务可以正常提供服务。

- 缓存

将高频访问的数据存储到读取效率较高的内存数据库，提升系统响应速度，缓解关系数据库的访问压力。 

- 异步

使用消息队列对部分不需要实时响应的业务进行异步处理，可以加快系统的响应速度，消除并发的访问高峰。

- 数据库读写分离

主数据库负责数据定义，从数据库负责数据读取，针对主从数据数据库可以通过配置分别提升其读写性能。

- 冗余 

服务器资源冗余： 每个服务在供正常使用的前提下，再多部署几台服务器，一方面可以处理突发的访问增长，另一方面在部分结点出现问题的时候可以保证系统正常使用。 

数据冗余： 数据库做实时的主从备份，并每隔一段时间做一次冷备份。主从复制可以保证主服务器出现问题时迅速切换到从服务器，冷备份可以保证当出现数据丢失时，可以使用最近时间点的备份数据进行恢复。

- 自动化

自动化运维：一键部署，减少人工操作可能出现的失误。
自动化测试：代码提交后及时发现问题，也可以规定必须测试完全通过才可以进行代码合并和发布上线。 
自动化监控报警：及时发现系统运行中的问题并通知给相关负责人。

- CDN

静态资源通过CDN服务器提供服务，不同地区会从最近的服务器读取数据，极大提交数据访问速度。

