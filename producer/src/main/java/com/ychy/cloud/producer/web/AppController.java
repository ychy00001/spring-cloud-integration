package com.ychy.cloud.producer.web;


import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ychy.cloud.producer.api.ProducerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class AppController implements ProducerApi {

    @Value("${isTest:false}")
    private boolean isTest;

    @GetMapping(path = "/")
    public String hello(){
        return "I'm producer!";
    }


    @GetMapping(path = "/conf")
    public boolean conf(){
        return isTest;
    }

    /**
     * 对外暴露端口
     * @return
     */
    @Override
    public String producer(){
        System.out.println("Be visited!");
        return "im producer";
    }

    @GetMapping(path = "/test")
    public String test(){
        ContextUtil.enter("/test", "test");
        Entry entry = null;
        // 务必保证 finally 会被执行
        try {
            // 资源名可使用任意有业务语义的字符串，注意数目不能太多（超过 1K），超出几千请作为参数传入而不要直接作为资源名
            // EntryType 代表流量类型（inbound/outbound），其中系统规则只对 IN 类型的埋点生效
            entry = SphU.entry("/test");
            // 被保护的业务逻辑
            return "test";
        } catch (BlockException ex) {
            // 资源访问阻止，被限流或被降级
            // 进行相应的处理操作
            return "被流控或者授权限制";
        } catch (Exception ex) {
            // 若需要配置降级规则，需要通过这种方式记录业务异常
            Tracer.traceEntry(ex, entry);
            return "系统异常";
        } finally {
            // 务必保证 exit，务必保证每个 entry 与 exit 配对
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
