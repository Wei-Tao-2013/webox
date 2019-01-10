package com.webox;

import com.webox.config.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(scanBasePackages={WBConfiguration.WBSERVICE})
//@ImportResource("classpath:mongo-config.xml")
public class WBConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(WBConfiguration.class);

	static final String WBSERVICE = "com.webox";

	@Value("${encrypted.tokenSecret}")
	private String tokenSecret;

	@Bean
	@SuppressWarnings("rawtypes")
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		JwtFilter jwtFilter = new JwtFilter();
		jwtFilter.setTokenSecret(tokenSecret);
		registrationBean.setFilter(jwtFilter);
		registrationBean.addUrlPatterns("/wb/*");
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(WBConfiguration.class, args);
	}

}
