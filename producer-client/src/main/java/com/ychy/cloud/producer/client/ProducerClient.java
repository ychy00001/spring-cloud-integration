package com.ychy.cloud.producer.client;

import com.ychy.cloud.producer.api.ProducerApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "service-producer")
public interface ProducerClient extends ProducerApi {
}
