package com.codelogium.exchangerateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

import com.codelogium.exchangerateservice.config.AppConfig;

@SpringBootApplication
public class ExchangerateserviceApplication {

	
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		
		//Register the config class
		context.register(AppConfig.class);

		//Refressh the context so the bean can be load
		context.refresh();

		//Retrieve the AppConfig bean
		AppConfig appConfig = context.getBean(AppConfig.class);

		//Get the base url from the bean
		String baseUrl = appConfig.getBaseUrl();

		System.out.println("Base Url: " + baseUrl);

		context.close();
		SpringApplication.run(ExchangerateserviceApplication.class, args);
	}

}
