package com.ychy.cloud.consumer.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础Nacos服务权重定义Ribbon负载均衡规则
 */
@Slf4j
public class WeightRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try{
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            String serviceName = baseLoadBalancer.getName();
            // 获取nacos注册中心
            NamingService namingServer = discoveryProperties.namingServiceInstance();
            // 获取一个健康的实例
            Instance instance = namingServer.selectOneHealthyInstance(serviceName);
            // 返回实例
            return new NacosServer(instance);
        }catch (NacosException e){
            log.error(e.getErrMsg());
        }
        return null;
    }
}
