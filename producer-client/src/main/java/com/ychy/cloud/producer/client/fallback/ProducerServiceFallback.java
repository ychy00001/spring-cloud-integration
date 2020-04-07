package com.ychy.cloud.producer.client.fallback;

import com.ychy.cloud.producer.client.ProducerClient;

/**
 * 限流或者降级返回异常
 */
public class ProducerServiceFallback implements ProducerClient {
    @Override
    public String producer() {
        return "被流控或降级了";
    }
}
