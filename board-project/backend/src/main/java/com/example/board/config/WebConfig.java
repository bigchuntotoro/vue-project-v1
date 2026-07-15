package com.example.board.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.yml 에 설정된 파일 저장 절대경로를 주입받습니다.
    // 설정 파일에 없다면 기본적으로 'C:/upload/' 경로를 타겟팅합니다.
    @Value("${file.dir:C:/upload/}")
    private String fileDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // 🎯 스프링 정적 리소스 핸들러로 파일 경로 다이렉트 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = fileDir.endsWith("/") ? fileDir : fileDir + "/";

        // 브라우저의 '/api/images/원본파일명.png' 요청을 -> 로컬 'file:///C:/upload/원본파일명.png' 파일로 연결
        registry.addResourceHandler("/api/images/**")
                .addResourceLocations("file:///" + uploadPath);
    }
}