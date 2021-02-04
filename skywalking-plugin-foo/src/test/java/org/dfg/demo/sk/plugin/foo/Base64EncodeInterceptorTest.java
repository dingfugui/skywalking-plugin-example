package org.dfg.demo.sk.plugin.foo;


import org.apache.commons.codec.binary.Base64;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractTracingSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.context.trace.TraceSegment;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.test.helper.SegmentHelper;
import org.apache.skywalking.apm.agent.test.tools.*;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.apache.skywalking.apm.agent.test.tools.SpanAssert.assertComponent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(TracingSegmentRunner.class)
public class Base64EncodeInterceptorTest {

    @SegmentStoragePoint
    private SegmentStorage segmentStorage;

    @Rule
    public AgentServiceRule serviceRule = new AgentServiceRule();

    private Base64EncodeInterceptor interceptor;

    @Mock
    private Base64 client;

    private byte[] param;

    private Object[] allArguments;
    private Class[] argumentTypes;

    private EnhancedInstance enhancedInstance = new EnhancedInstance() {

        private Object object;

        @Override
        public Object getSkyWalkingDynamicField() {
            return object;
        }

        @Override
        public void setSkyWalkingDynamicField(Object value) {
            this.object = value;
        }
    };

    @Before
    public void setUp() throws Exception {
        param = "abc".getBytes(StandardCharsets.UTF_8);
        allArguments = new Object[]{
                param
        };
        argumentTypes = new Class[]{
                param.getClass()
        };
        interceptor = new Base64EncodeInterceptor();
    }

    @Test
    public void testOnConstruct() {
        //interceptor.onConstruct(enhancedInstance, allArguments);
        //assertThat(enhancedInstance.getSkyWalkingDynamicField(), is(allArguments[1]));
    }

    @Test
    public void testMethodsAround() throws Throwable {
        //interceptor.onConstruct(enhancedInstance, allArguments);
        interceptor.beforeMethod(enhancedInstance, null, allArguments, argumentTypes, null);

        String result = "result";
        interceptor.afterMethod(enhancedInstance, null, allArguments, argumentTypes, result);

        assertThat(segmentStorage.getTraceSegments().size(), is(1));
        TraceSegment traceSegment = segmentStorage.getTraceSegments().get(0);
        List<AbstractTracingSpan> spans = SegmentHelper.getSpans(traceSegment);

        assertSpan(spans.get(0));
        SpanAssert.assertOccurException(spans.get(0), false);
    }

    @Test
    public void testMethodsAroundError() throws Throwable {

    }

    private void assertSpan(AbstractTracingSpan span) {
        assertComponent(span, Base64EncodeInterceptor.BASE64);
        SpanAssert.assertLayer(span, SpanLayer.HTTP);
        SpanAssert.assertTag(span, 0, Arrays.toString(param));
        SpanAssert.assertTag(span, 1, "result");
        assertThat(span.isExit(), is(true));
        assertThat(span.getOperationName(), is("base64"));
    }

    @Test
    public void testException() throws Throwable {

    }
}
