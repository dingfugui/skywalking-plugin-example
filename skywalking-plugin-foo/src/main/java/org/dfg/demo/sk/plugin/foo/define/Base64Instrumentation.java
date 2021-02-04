package org.dfg.demo.sk.plugin.foo.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * #3，告诉skywalking拦截哪些方法，并指定拦截器
 * ClassEnhancePluginDefine 父类
 * ClassInstanceMethodsEnhancePluginDefine 实例方法
 * ClassStaticMethodsEnhancePluginDefine 静态方法
 */
public class Base64Instrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "org.apache.commons.codec.binary.Base64";

    private static final String INTERCEPT_CLASS = "org.dfg.demo.sk.plugin.foo.Base64EncodeInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        return NameMatch.byName(ENHANCE_CLASS);
    }


    /**
     * 拦截构造器
     *
     * @return
     */
    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return null;
    }

    /**
     * 拦截方法
     * InstanceMethodsAroundInterceptor 实例方法
     * InstanceConstructorInterceptor 构造方法
     * StaticMethodsAroundInterceptor 静态方法
     *
     * @return
     */
    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        //拦截实例方法
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        //拦截方法，支持多种匹配规则
                        return named("encodeToString")
//                                .and(takesArguments(1))
//                                .and(takesArguments(byte[].class))
                                ;
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPT_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }
}
