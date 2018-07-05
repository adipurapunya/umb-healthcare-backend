package com.umb.cppbt.rekammedik.rekammedik.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration 
public class WebConfig extends WebMvcConfigurerAdapter
{ 
	@Override 
	public void addResourceHandlers(final ResourceHandlerRegistry registry) 
	{ 
		registry.addResourceHandler("/*.js/**").addResourceLocations("/static/"); 
		registry.addResourceHandler("/*.css/**").addResourceLocations("/static/");
	}
	
	@Override 
	public void addViewControllers(ViewControllerRegistry registry)
	{ 
		registry.addViewController("/").setViewName("login"); 
		registry.addViewController("/login").setViewName("login"); 
	} 
	
	@Bean 
	public InternalResourceViewResolver setupViewResolver()
	{ 
		InternalResourceViewResolver resolver = new InternalResourceViewResolver(); 
		resolver.setPrefix ("/static/app/views"); 
		resolver.setSuffix (".html");
		resolver.setViewClass (JstlView.class); return resolver; 
	} 
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") 
                .allowedMethods("GET", "POST", "PUT","DELETE");
            }
        };
    }
	

}