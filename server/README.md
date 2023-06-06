## 类
### 书写顺序：
1. id
2. 外部id
3. 自身属性
4. 状态
5. 权限
6. 时间

## 服务
### 书写顺序
1. 查
2. 更
3. 增
4. 删

## 接口
### 参数
1. param不写@， 其他都写
2. 所有属性都要求默认值


## docker
+ docker run -itd --name redis -p 6379:6379 redis --requirepass "124609"
+ docker run -itd --name mongo -p 27017:27017 mongo --auth
+ docker run -itd --name rabbitmq -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=124609 rabbitmq:management
