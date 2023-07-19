package com.sarfa.mywiki;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.sarfa.mywiki.mapper")
public class MyWikiApplication {
    private static final Logger LOG = LoggerFactory.getLogger(MyWikiApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MyWikiApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }
    //启动图案设置
    //http://patorjk.com/software/taag/#p=display&h=1&v=1&f=Big&t=jiawa%0A
}
