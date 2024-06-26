package com.nhnacademy.front.config;

import com.nhnacademy.front.interceptor.AddAuthorityToModelInterceptor;
import com.nhnacademy.front.interceptor.LoggedInUserAccessInterceptor;
import com.nhnacademy.front.interceptor.LoginCheckInterceptor;
import com.nhnacademy.front.interceptor.NotificationTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.Map;

/**
 * Spring boot 의 전반적인 Web 설정을 위한 Configuration
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AddAuthorityToModelInterceptor addAuthorityToModelInterceptor;
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final LoggedInUserAccessInterceptor loggedInUserAccessInterceptor;

    private final NotificationTokenInterceptor notificationTokenInterceptor;

    /**
     * Url 과 html View 를 연결하는 메서드.
     *
     * @param registry URL 경로나 패턴을 뷰 컨트롤러에 매핑하여 구성된 상태 코드와 뷰로 응답을 렌더링.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/auth/login").setViewName("login");
        registry.addViewController("/auth/register").setViewName("register");
        registry.addViewController("/admin/rule/register").setViewName("rule-register");
    }

    /**
     * RestTemplate 을 생성하는 빈을 설정하는 메서드.
     *
     * @param restTemplateBuilder RestTemplate 를 구성하고 생성할 수 있는 빌더.
     * @return RestTemplate 객체
     */
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setReadTimeout(Duration.ofMillis(30000))
                .setConnectTimeout(Duration.ofMillis(30000))
                .build();
    }

    /**
     * 인터셉터를 등록하는 메서드.
     *
     * @param registry URL 경로나 패턴을 뷰 컨트롤러에 매핑하여 구성된 상태 코드와 뷰로 응답을 렌더링.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(addAuthorityToModelInterceptor);
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**", "")
                .excludePathPatterns("/auth/login", "/login", "/register", "/auth/register", "/error", "/oauth2/**", "/css/**", "/js/**", "/img/**");
        registry.addInterceptor(loggedInUserAccessInterceptor)
                .addPathPatterns("/auth/login", "/auth/register");
        registry.addInterceptor(notificationTokenInterceptor)
                .excludePathPatterns("auth/login", "/auth/register", "/login", "/register", "/control", "/device/settings", "/detect/settings");
    }

    /**
     * 장치에 해당하는 그림을 매칭하기 위한 빈 메서드
     *
     * @return
     */
    @Bean
    public Map<String, String> deviceIconMap() {
        return Map.of(
                "light", "lightbulb",
                "airconditioner", "air",
                "aircleaner", "air_purifier",
                "aiMode", "settings_applications");
    }
}
