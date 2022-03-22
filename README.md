# wormForWB
本项目受朋友所托，从github其他的微博博主动态监控的python项目中获得了一些启发，目的是监控指定博主的动态并发出提醒。
## 项目开发计划
通过一天的沟通和确认，在保证技术和经济可行性的情况下捋清了需求。

整体项目分为三步走：

- **lite版**：即初版，已于2022年3月10日开发完成，服务轻量化，不依赖NOSQL或SQL数据库，仅使用JVM堆栈对数据进行存储，优点是服务部署简单，不使用其他中间件或数据服务，但同样带来的缺点就是功能简单，且仅支持单人订阅，但仅作为动态监控的提示工具已经满足了业务需求，故起名为lite，即轻量版（笑
- **pro版**：接下来的2.0版本，预计引入mysql数据库用于存储多组用户邮箱和订阅博主uid的关系数据，以实现多人使用，并且由于引入了数据库，部署难度将加大，故计划pro版本将部署在云服务器上直接提供服务
- **max版**：未来的3.0版本，在功能上将添加对博主正文和图片的存储并导出到外部文本文件，通过引入nignx，实现用户点击链接查看历史推送内容的功能，该功能存在技术上的疑问

## 项目详细设计

- 初版已完成开发并登录服务器投入使用，不依赖任何数据支持，功能上仅支持一个邮箱订阅多个博主动态，且不支持数据持久化（都说了没用数据库啦🙄
- 再版使用mysql5.7作为数据持久化支持，引入mybatis和druid实现对数据库的整合，目前功能规划如下：
  1. 再版的初衷，是实现多组订阅关系，即多个邮箱订阅各自不同的多个博主，目前该功能已实现；
  2. 服务启动后，对监测过的动态进行存储，即支持**动态日志回看**，要点如下：
     - 每天监控完成后，将每个订阅组的内容包括文字和图片整理进一个文档，采用定时任务t+1，通过url或离线压缩包发送给用户（即根据订阅组的关系，内容是用户关注的所有博主的动态内容）
     - 记录用户关注的博主动态，依旧采用定时任务t+1（即根据博主个人的动态，内容是该博主的动态内容）

## 技术实现细节

日志回看功能：

- **创建日志文件夹定时任务**：每天执行定时任务，创建每日的日志文件夹，并在该文件夹下为每个用户创建对应的文件夹
- **创建压缩定时任务**：将暂时失效的文件夹打包压缩存储，节省服务器硬盘空间
- **数据库日志表自动分表**：动态日志需要存储至数据库以方便t-1导出到PDF文件中，数据库需要对日志表以月划分进行分表
- **PDF操作**：将日志从数据库取出后导出到PDF文件



## 开发日志

**2022/3/22**:

1. 预定功能全部实现，部署服务器开始测试
2. 动态查询依旧使用单线程for循环，性能待优化

**2022/3/18**：

1. 根据数据库存储的昨日动态，t+1生成动态日志，以pdf导出
2. 利用nginx实现url访问日志资源，并顺带反向代理
3. 实现分表，日志文件夹定时任务

**2022/3/11**：

1. pro版基础功能，依赖数据库的多组订阅关系已实现，但采用的for循环单线程，效率极低，后期需使用多线程或异步；对旧动态的判断还在使用初版的queue，导致每次服务启动后会先执行一次发送任务将服务开始前最新的动态存储后才能正常工作，待完善。

**2022/3/10**：

1. 初版开发已完成，经测试后出现原文链接位置不确定导致NPE，已解决，目前暂无新bug
2. 初版支持单个邮箱订阅多个博主动态，由于未采用数据库，不支持数据持久化
3. 拉取develop_pro分支，准备2.0版本开发
