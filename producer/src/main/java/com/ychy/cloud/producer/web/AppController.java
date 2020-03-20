package com.ychy.cloud.producer.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class AppController {

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

    @GetMapping(path = "/producer")
    public String producer(){
        System.out.println("Be visited!");
        return "visit producer";
    }
}
