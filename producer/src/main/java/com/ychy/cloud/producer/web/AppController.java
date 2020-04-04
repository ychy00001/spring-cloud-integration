package com.ychy.cloud.producer.web;


import com.ychy.cloud.producer.api.ProducerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class AppController implements ProducerApi {

    @Value("${isTest:false}")
    private boolean isTest;

    @GetMapping(path = "/")
    public String hello(){
        return "I'm producer!";
    }


    @GetMapping(path = "/conf")
    public boolean conf(){
        return isTest;
    }

    /**
     * 对外暴露端口
     * @return
     */
    @Override
    public String producer(){
        System.out.println("Be visited!");
        return "im producer";
    }
}
