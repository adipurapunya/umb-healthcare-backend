package com.umb.cppbt.rekammedik.rekammedik.config;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private SessionRegistry sessionRegistry;
	
	@Autowired
    ServletContext servletContext;
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	LogoutSuccess logoutSuccess;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/register/**", "/authenticate/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors().and()
		.authorizeRequests()
				.anyRequest().authenticated()
				.and().logout().logoutSuccessHandler(logoutSuccess).deleteCookies("JSESSIONID")
				.and()
				.csrf().disable()
				.addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
				.httpBasic();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() 
	{
	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
	    return sessionRegistry;
	}
	
	@Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() 
	{
        return new HttpSessionEventPublisher();
    }
	
}


