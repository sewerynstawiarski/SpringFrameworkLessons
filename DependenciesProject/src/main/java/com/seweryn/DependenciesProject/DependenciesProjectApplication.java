package com.seweryn.DependenciesProject;

import com.seweryn.DependenciesProject.Controllers.MyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DependenciesProjectApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(DependenciesProjectApplication.class, args);

		MyController myController = ctx.getBean(MyController.class);

		System.out.println("In the main method");

		System.out.println(myController.sayHello());
	}

}
