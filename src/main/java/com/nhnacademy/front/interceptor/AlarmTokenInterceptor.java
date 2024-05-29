package com.nhnacademy.front.interceptor;

import com.nhnacademy.front.utils.AccessTokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

/**
 * 특정상황을 제외하고 accessToken을 view에 attribute로 추가하는 interceptor
 */
@Component
public class AlarmTokenInterceptor implements HandlerInterceptor {

    /**
     * 요청이 처리되기 전에 호출되는 메서드로, 로그인 상태를 확인하고 처리를 결정.
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param handler 요청을 처리할 핸들러 객체
     * @param modelAndView 화면에 나타낼 model
     * @throws Exception 예외 발생 시
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (Objects.isNull(request.getCookies())) {
            return;
        }

        if (Objects.isNull(request.getCookies())) {
            return;
        }


        Cookie accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .findFirst()
                .orElse(null);


        if (!Objects.isNull(accessToken) && modelAndView != null) {

            ModelMap model = modelAndView.getModelMap();
            model.addAttribute("accessTokenInter", AccessTokenUtil.findAccessTokenInRequest(request));


        }
    }
}
