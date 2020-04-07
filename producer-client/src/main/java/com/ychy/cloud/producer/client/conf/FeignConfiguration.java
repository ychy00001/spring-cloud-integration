package com.ychy.cloud.producer.client.conf;

import com.ychy.cloud.producer.client.fallback.ProducerServiceFallback;
import org.springframework.context.annotation.Bean;

/**
 * 配置Feign的限流或者降级返回处理方法
 */
public class FeignConfiguration {
    @Bean
    public ProducerServiceFallback producerServiceFallback() {
        return new ProducerServiceFallback();
    }
}
