package com.ychy.cloud.producer.client;

import com.ychy.cloud.producer.api.ProducerApi;
import com.ychy.cloud.producer.client.conf.FeignConfiguration;
import com.ychy.cloud.producer.client.fallback.ProducerServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "service-producer", fallback = ProducerServiceFallback.class, configuration = FeignConfiguration.class)
public interface ProducerClient extends ProducerApi {
}
