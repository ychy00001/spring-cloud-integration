package com.ychy.cloud.producer.api;

import org.springframework.web.bind.annotation.GetMapping;

public interface ProducerApi {
    @GetMapping("/producer")
    String producer();
}
