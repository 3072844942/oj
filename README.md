![image-20221216202545117](./assets/image-20221216202545117.png)

<p align=center>
   基于Springboot + React 开发的前后端分离分布式在线评测平台
</p>
<p align="center">
   <a target="_blank" href="https://github.com/3072844942/nucoj">
      <img src="https://img.shields.io/hexpm/l/plug.svg"/>
      <img src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
      <img src="https://img.shields.io/badge/springboot-2.6.11-green"/>
      <img src="https://img.shields.io/badge/react-18.2.0-green"/>
      <img src="https://img.shields.io/badge/mongodb-3.3.6-green"/>
      <img src="https://img.shields.io/badge/redis-6.1.9-green"/>
      <img src="https://img.shields.io/badge/rabbitmq-3.8.5-green"/>
      <img src="https://img.shields.io/badge/springcloud-2021.0.4-green"/>
   </a>
</p>


### 在线地址

**项目链接**：[https://www.snak.space/](https://www.snak.space/)  

**后台地址**：[https://www.admin.snak.space/ ](https://www.admin.snak.space/) 
**测试账号**： 账号: test@qq.com, 密码: Aa1234567。  
**Github地址**：[https://github.com/3072844942/nucoj  ](https://github.com/3072844942/nucoj)
**在线接口地址**: [https://www.snak.space/api/doc.html ](https://www.snak.space/api/doc.html) 

### 目录结构

+ react 
    + nucoj：前台
    + nucoj_admin：后台
+ spring
    + cloud：eureka注册中心
    + judge_core：判题核心
    + judge_server：判题服务器
    + nucoj_spring：主服务器

### 技术简介

**前端**：react + redux + react router + axios + antd + echarts

**后端**：SpringBoot + SpringCloud + nginx + docker + SpringSecurity + Swagger2 + Redis + RebbitMQ

### 开发环境

| 开发工具           | 说明             |
| ------------------ | ---------------- |
| IDEA               | Java开发工具IDE  |
| WebStorm           | React开发工具IDE |
| Windows PowerShell | 杂项             |

| 开发环境 | 版本    |
| -------- | ------- |
| Java     | 1.8     |
| MongoDB  | 4.4.17  |
| Redis    | 3.0.504 |
| RabbitMQ | 3.11.2  |

| 运行环境 | 说明                  |
| -------- | --------------------- |
| 服务器   | 腾讯云2核4G CentOS7.6 |

### 项目截图

![image-20221216211400506](./assets/image-20221216211400506.png)

![image-20221216211743586](./assets/image-20221216211743586.png)

![image-20221216211833582](./assets/image-20221216211833582.png)

![image-20221216211901421](./assets/image-20221216211901421.png)

![image-20221216211909048](./assets/image-20221216211909048.png)

### 项目亮点

- 前台参考牛客/PTA/洛谷，美观简洁，响应式体验好。
- 后台参考"element-admin"设计，侧边栏，历史标签。
- 采用Markdown编辑器，写法简单。
- 评论支持表情、GIF动图输入回复等，样式参考Valine。
- 前后端分离部署，适应当前潮流。
- 接入第三方登录，减少注册成本。
- 支持代码高亮和复制，图片预览，深色模式等功能，提升用户体验。
- 搜索文章支持高亮分词，响应速度快。
- 新增在线聊天室，支持撤回、语音输入、统计未读数量等功能。
- 新增aop注解实现操作日志功能。
- 支持动态权限修改，采用RBAC模型，前端菜单和后台权限实时更新。
- 代码支持多种搜索模式（Elasticsearch或MYSQL），支持多种上传模式（OSS或本地），可支持配置。Elasticsearch占内存较高，如果服务器配置太低，不建议使用。
- 新增网站导航功能，页面优雅美观。可自行添加自己常用的网站进行分类和排序。
- 新增登录日志功能，随时查看用户的登录信息。
- 前台页面重新布局重构，页面布局更加简洁，首页加载速度更快。
- 新增了监控用户是否授权的功能、增加了用户的体验性。
- 增加图片删除后也将该路径的图片删除的功能、大大提高了文件存储的利用率。
- 将接口进行**axios**二次封装、更利于接口的请求和响应。
- 代码遵循阿里巴巴开发规范，利于开发者学习。

### 快速开始

详情见每个项目的README.md文件

### 项目总结

做一个作者的新手入门项目， 作者认为其完成了一个OJ应当完成的任务， 项目所用的技术栈也覆盖较广。但是，难以成为替代一个成熟的， 能够被学校所使用的OJ项目。UI界面，权限管理， 对故障的应急处理均有不足， 做的不好的地方请见谅。
