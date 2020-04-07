package com.ychy.cloud.consumer.conf;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * 修改默认负载均衡算法
     * TODO 可用配置文件形式实现 查看bootstrap.properties文件
     */
//    @Bean
//    public IRule randomRule(){
//        return new RandomRule();
//    }
}
