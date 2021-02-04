package org.dfg.demo.sk.plugin.foo;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.tag.StringTag;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.Component;
import org.apache.skywalking.apm.network.trace.component.OfficialComponent;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * #4，skywalking拦截到指定方法后回调
 * 在这里面获取调用情况如方法、参数等，并记录span
 */
public class Base64EncodeInterceptor implements InstanceMethodsAroundInterceptor {

    public static final OfficialComponent BASE64 = new OfficialComponent(301, "BASE64");

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                             MethodInterceptResult result) throws Throwable {

        //创建span
        AbstractSpan span = ContextManager.createLocalSpan("base64.encode");
        //设置组件类型
        span.setComponent(BASE64);
        //获取参数
        byte[] param = (byte[]) allArguments[0];
        //记录span tag
        new StringTag("source").set(span, Arrays.toString(param));
        //记录span
        SpanLayer.asHttp(span);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                              Object ret) throws Throwable {
        if (ret != null) {
            AbstractSpan span = ContextManager.activeSpan();
            //span.errorOccurred();
            new StringTag("result").set(span, String.valueOf(ret));
        }
        //结束span
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                      Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan abstractSpan = ContextManager.activeSpan();
        abstractSpan.log(t);
    }
}
