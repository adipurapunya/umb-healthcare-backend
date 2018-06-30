package com.umb.cppbt.rekammedik.rekammedik;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.umb.cppbt.rekammedik.rekammedik.jpa.JpaConfiguration;

@Import({JpaConfiguration.class})
@SpringBootApplication(scanBasePackages={"com"})
public class RekammedikApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "prod");
		SpringApplication.run(RekammedikApplication.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
	    return application.sources(new Class[] { RekammedikApplication.class });
	}
	
	public void onStartup(ServletContext container) throws ServletException {
		System.setProperty("spring.profiles.active", "prod"); 
		super.onStartup(container);
	}

}
