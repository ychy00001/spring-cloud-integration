package com.ychy.cloud.producer.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@ControllerAdvice(basePackages = "com.ychy.cloud.producer.web")
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = BlockException.class)
    public String blockExceptionHandler(BlockException blockException) {
        return new JSONObject().fluentPut("code", 10001)
                .fluentPut("msg", "请求被拦截，拦截类型为 " + blockException.getClass().getSimpleName()).toJSONString();
    }

}
