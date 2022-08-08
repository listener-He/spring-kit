package org.hehh.framework.gateway.routing.nacos;

import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 版本号权重路由
 *
 * @author HeHui
 * @date 2022-07-15 11:39
 */
public class NacosVersionRoundRobinLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final Log log = LogFactory.getLog(NacosVersionRoundRobinLoadBalancer.class);

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final String serviceId;

    private final String clusterName;

    public NacosVersionRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, String clusterName) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.clusterName = clusterName;
    }

    /**
     * Choose the next server based on the load balancing algorithm.
     *
     * @param request - an input request
     * @return - mono of response
     */
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        HttpHeaders headers = (HttpHeaders) request.getContext();
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get().next().map(list -> getInstanceResponse(list, headers));
    }


    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances, HttpHeaders headers) {
        if (serviceInstances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }

        List<ServiceInstance> instancesToChoose = serviceInstances;
        try {
            if (StringUtils.hasLength(clusterName)) {
                List<ServiceInstance> sameClusterInstances = serviceInstances.stream()
                    .filter(serviceInstance -> {
                        String cluster = serviceInstance.getMetadata()
                            .get("nacos.cluster");
                        return Objects.equals(cluster, clusterName);
                    }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                    instancesToChoose = sameClusterInstances;
                }
            }
        } catch (Exception e) {
            log.warn("VersionRoundRobinLoadBalancer error", e);
            return null;
        }


        List<ServiceInstance> serviceVersionInstances = instancesToChoose.stream()
            .filter(instance -> {
                //根据请求头中的版本号信息，选取注册中心中的相应服务实例
                String version = headers.getFirst("Version");
                if (version != null) {
                    return version.equals(instance.getMetadata().get("version"));
                } else {
                    return true;
                }
            }).collect(Collectors.toList());
        if (serviceVersionInstances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        ServiceInstance instance = NacosBalancer
            .getHostByRandomWeight3(instancesToChoose);

        return new DefaultResponse(instance);
    }
}
