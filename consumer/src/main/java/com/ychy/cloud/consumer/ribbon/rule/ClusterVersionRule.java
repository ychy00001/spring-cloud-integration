package com.ychy.cloud.consumer.ribbon.rule;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Nacos服务所在集群以及版本号规则就近调用规则
 * 用于rabbon负载均衡配置，请查看bootstrap.properties
 */
@Slf4j
public class ClusterVersionRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try{
            // 获取本地集群名称
            String localClusterName = discoveryProperties.getClusterName();
            List<Instance> sameClusterAndSameVersionInstanceList = getTheSameClusterAndSameVersionInstance(discoveryProperties);
            Instance toBeChooseInstance;

            if(sameClusterAndSameVersionInstanceList.isEmpty()){
                toBeChooseInstance = crossClusterAndSameVersionInvoke(discoveryProperties);
            }else{
                toBeChooseInstance = WeightBalancer.chooseInstanceByRandomWeight(sameClusterAndSameVersionInstanceList);
                log.info("当前微服务所在集群:{},当前微服务版本:{},被调用集群:{},被调用版本:{},被调用host:{},被调用端口:{}",
                        localClusterName,discoveryProperties.getMetadata().get("current-version"),toBeChooseInstance.getClusterName(),
                        toBeChooseInstance.getMetadata().get("current-version"),toBeChooseInstance.getIp(),toBeChooseInstance.getPort());
            }
            return new NacosServer(toBeChooseInstance);
        }catch (NacosException e){
            log.error(e.getErrMsg());
        }
        return null;
    }

    private List<Instance> getTheSameClusterAndSameVersionInstance(NacosDiscoveryProperties discoveryProperties) throws NacosException{
        String currentClusterName = discoveryProperties.getClusterName();
        String currentVersion = discoveryProperties.getMetadata().get("current-version");
        List<Instance> allInstance = getAllInstance(discoveryProperties);
        List<Instance> sameClusterAndSameVersionInstanceList = new ArrayList<>();
        for (Instance instance : allInstance){
            if(StringUtils.endsWithIgnoreCase(instance.getClusterName(),currentClusterName) &&
            StringUtils.endsWithIgnoreCase(instance.getMetadata().get("current-version"),currentVersion)){
                sameClusterAndSameVersionInstanceList.add(instance);
            }
        }
        return sameClusterAndSameVersionInstanceList;
    }

    private Instance crossClusterAndSameVersionInvoke(NacosDiscoveryProperties discoveryProperties) throws NacosException{
        List<Instance> crossClusterAndSameVersionInstanceList = getCrossClusterAndSameVersionInstanceList(discoveryProperties);
        String currentClusterName = discoveryProperties.getClusterName();
        String currentVersion = discoveryProperties.getMetadata().get("current-version");
        Instance toBeChooseInstance;
        if(crossClusterAndSameVersionInstanceList.isEmpty()){
            log.error("找不到当前版本服务，当前版本{}",currentVersion);
            throw new RuntimeException("找不到相同版本服务");
        }else{
             toBeChooseInstance = WeightBalancer.chooseInstanceByRandomWeight(crossClusterAndSameVersionInstanceList);
            log.info("跨集群调用，当前微服务所在集群:{},当前微服务版本:{},被调用集群:{},被调用版本:{},被调用host:{},被调用端口:{}",
                    currentClusterName,discoveryProperties.getMetadata().get("current-version"),toBeChooseInstance.getClusterName(),
                    toBeChooseInstance.getMetadata().get("current-version"),toBeChooseInstance.getIp(),toBeChooseInstance.getPort());
        }
        return toBeChooseInstance;
    }

    private List<Instance> getCrossClusterAndSameVersionInstanceList(NacosDiscoveryProperties discoveryProperties) throws NacosException {
        String currentVersion = discoveryProperties.getMetadata().get("current-version");
        List<Instance> allInstance = getAllInstance(discoveryProperties);
        List<Instance> sameClusterAndSameVersionInstanceList = new ArrayList<>();
        for (Instance instance : allInstance) {
            if (StringUtils.endsWithIgnoreCase(instance.getMetadata().get("current-version"), currentVersion)) {
                sameClusterAndSameVersionInstanceList.add(instance);
            }
        }
        return sameClusterAndSameVersionInstanceList;
    }

    private List<Instance> getAllInstance(NacosDiscoveryProperties discoveryProperties) throws NacosException{
        // 获取nacos注册中心
        NamingService namingServer = discoveryProperties.namingServiceInstance();
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String serviceName = baseLoadBalancer.getName();
        return namingServer.getAllInstances(serviceName);
    }
}
