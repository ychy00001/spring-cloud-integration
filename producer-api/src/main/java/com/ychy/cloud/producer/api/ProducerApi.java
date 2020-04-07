package com.ychy.cloud.producer.api;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 定义生产者统一接口
 */
public interface ProducerApi {
    @GetMapping("/producer")
    String producer();
}
