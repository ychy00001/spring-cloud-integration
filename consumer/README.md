# 消费者
## 注册中心名称
```
service-consumer
```

## 注册中心配置文件
```
service-consumer.properties
```

## 测试配置中心
```
// 配置中心在配置文件中新增配置
isTest=true
// 访问地址
http://localhost:8082/conf
```

## 客户端负载均衡Ribbon使用
```
restTemplate.getForObject("http://service-producer/producer/", String.class);
```

## 客户端负载均衡Feign使用
```

```