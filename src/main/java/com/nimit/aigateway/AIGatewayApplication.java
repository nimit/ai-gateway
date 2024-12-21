package com.nimit.aigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AIGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIGatewayApplication.class, args);

		// ? TO VERIFY BEAN GENERATION
		// var ctx = SpringApplication.run(AIGatewayApplication.class, args);
		// String[] beanNames = ctx.getBeanDefinitionNames();
		// System.out.println("=== All registered beans ===");
		// for (String beanName : beanNames) {
		// System.out.println(beanName);
		// }
	}

}