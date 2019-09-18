package cn.weChat.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan(basePackages = {
        "cn.weChat.controller",
        "cn.weChat.common",
        "cn.weChat.model",
        "cn.weChat.start"
})
@MapperScan(basePackages = {
        "cn.weChat.model.mapper"
})
@SpringBootApplication
@EnableSwagger2
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
