package org.dfg.demo.foo;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@RestController
@Configuration
@SpringBootApplication
public class FooApplication {
    /**
     * jar启动：
     * java -jar -javaagent:D:\workspace\skywalking-plugin-example\apache-skywalking-apm-bin-es7\agent\skywalking-agent.jar
     * -DSW_AGENT_NAME=foo-service foo-service-1.0.0.jar
     * idea启动：
     * 需要修改idea配置为：委托给maven
     * -javaagent:D:\workspace\skywalking-plugin-example\apache-skywalking-apm-bin-es7\agent\skywalking-agent.jar
     * -Dskywalking_config=D:\workspace\skywalking-plugin-example\apache-skywalking-apm-bin-es7\agent\config\agent.config
     * -DSW_AGENT_NAME=foo-servic
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(FooApplication.class);
    }

    @Autowired
    JdbcTemplate jdbc;

    @RequestMapping("foo")
    public String foo(@RequestParam("p") String s) {
        jdbc.execute("select 1");
        return new Base64().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

}
