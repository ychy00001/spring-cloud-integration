package com.ychy.cloud.consumer.web;


import com.ychy.cloud.producer.client.ProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
public class AppController {

    /**
     * 注入Feign客户端
     */
    @Autowired
    private ProducerClient producerClient;

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
     * 使用Rabbon获取远程服务
     */
    @GetMapping(path = "/visitProducer")
    public String accessProducerByRabbon(){
        return restTemplate.getForObject("http://service-producer/producer/", String.class);
    }

    /**
     * 使用Feign获取远程服务
     */
    @GetMapping(path = "/visitFeignProducer")
    public String accessProducerByFeign(){
        return producerClient.producer();
    }
}
