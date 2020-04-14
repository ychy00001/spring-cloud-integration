# Spring cloud 2 微服务集成

## 项目结构
1. consumer 消费者服务
2. producer 生产者服务
3. producer-api 统一生产者接口
4. producer-client 生产者对外提供jar包，提供访问生产者FeignClient

## 使用步骤

### nexus
```
cd middleware/nexus
docker-compose up -d
// 访问
http://localhost:9090
username:admin
密码请查看（初始化后重置）：
cat middleware/nexus/nexus-data/admin.password
当前初始化为：admin
```

### 注册中心、配置中心 Naocos
#### 配置nacos Mysql数据源
1. 创建数据库及表 https://github.com/alibaba/nacos/blob/master/distribution/conf/nacos-mysql.sql
2. 更application.properties文件
```
### If user MySQL as datasource:
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://1.1.1.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=user
db.password=password
```

#### 启动nacos
```maven
cd middleware/nacos
mvn -Prelease-nacos -Dmaven.test.skip=true clean install -U
cd distribution/target/nacos-server-1.2.0/nacos/bin
// 单节点方式启动 集群方式参考官网
./startup.sh -m standalone
```

#### 访问nacos
```
http://localhost:8848/nacos/#/login
username: nacos
password: nacos
``` 

#### 启动生产者并访问
```
http://localhost:8081/producer
```

#### 启动消费者并访问
```
// 访问自己
http://localhost:8082/
// 访问生产者
http://localhost:8082/visitProducer
```

#### 配置中心配置
```
// 使用如下规则在配置中心配置内容
${prefix}-${spring.profile.active}.${file-extension}
- prefix 默认为 spring.application.name 的值，也可以通过配置项 spring.cloud.nacos.config.prefix来配置。
- spring.profile.active 即为当前环境对应的 profile，详情可以参考 Spring Boot文档。 注意：当 spring.profile.active 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 ${prefix}.${file-extension}
- file-exetension 为配置内容的数据格式，可以通过配置项 spring.cloud.nacos.config.file-extension 来配置。目前只支持 properties 和 yaml 类型。
```

#### 测试配置中心
```
// 配置的默认Data Id为
${spring.application.name}.properties
// 配置中心在配置文件中新增配置
isTest=true
// 访问地址
http://localhost:8082/conf
```

### 熔断限流 Sentinel
```
// 根目录执行
mvn clean package -Dmaven.test.skip=true
java -Dserver.port=9001 \
-Dcsp.sentinel.dashboard.server=localhost:9001 \
-Dproject.name=sentinel-dashboard \
-Dnacos.address=localhost:8848 \
-jar target/sentinel-dashboard.jar
```
#### 访问
localhost:9001
账号：sentinel
密码：sentinel