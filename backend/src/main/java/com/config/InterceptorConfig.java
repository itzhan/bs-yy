package com.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);
	@Value("${upload.path}")
	private String uploadPath;

	@Bean
	public AuthorizationInterceptor getAuthorizationInterceptor() {
		return new AuthorizationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAuthorizationInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/static/**", "/upload/**", "/favicon.ico");
	}

	/**
	 * 静态资源
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String path = "file:" + Paths.get(uploadPath).toAbsolutePath() + "/";
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/resources/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/admin/")
				.addResourceLocations("classpath:/front/")
				.addResourceLocations("classpath:/front-pc/")
				.addResourceLocations("classpath:/public/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations(path);
	}

	/**
	 * 初始化上传目录
	 */
	@PostConstruct
	public void init() {
		try {
			File directory = new File(uploadPath);
			if (!directory.exists()) {
				directory.mkdirs();
				log.info("创建上传目录: {}", directory.getAbsolutePath());
			}
		} catch (Exception e) {
			log.error("创建上传目录失败", e);
		}
	}
}
