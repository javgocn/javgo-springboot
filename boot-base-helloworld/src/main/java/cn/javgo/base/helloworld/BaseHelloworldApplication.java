package cn.javgo.base.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Step1: 添加 @RestController 注解
@SpringBootApplication
public class BaseHelloworldApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseHelloworldApplication.class, args);
	}

	// Step2: 添加 @RequestMapping 注解, 并指定请求路径
	@RequestMapping("/hello")
	public String hello() {
		// Step3: 返回字符串
		return "Hello, World!";
	}
}
