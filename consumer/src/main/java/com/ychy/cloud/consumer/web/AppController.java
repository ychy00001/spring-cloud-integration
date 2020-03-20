package com.ychy.cloud.consumer.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
public class AppController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${isTest:false}")
    private boolean isTest;

    @GetMapping(path = "/")
    public String hello(){
        return "I'm consumer!";
    }

    @GetMapping(path = "/conf")
    public boolean conf(){
        return isTest;
    }

    /**
     * 使用RestTemplate获取远程服务
     */
    @GetMapping(path = "/visitProducer")
    public String accessProducer(){
        return restTemplate.getForObject("http://service-producer/producer/", String.class);
    }

}
