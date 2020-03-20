package com.ychy.cloud.consumer.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * 根据权重随机选择一个Nacos服务实例
 */
public class WeightBalancer extends Balancer {

    public static Instance chooseInstanceByRandomWeight(List<Instance> list){
        return getHostByRandomWeight(list);
    }
}
