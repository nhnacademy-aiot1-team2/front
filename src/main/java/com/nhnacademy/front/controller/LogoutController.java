package com.nhnacademy.front.controller;

import com.nhnacademy.front.adaptor.UserAdapter;
import com.nhnacademy.front.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

/**
 * 로그아웃 처리 Controller 클래스
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LogoutController {
    private final UserAdapter userAdapter;
    private final RedisUtil redisUtil;

    /**
     * 로그아웃 처리 핸들러 메서드
     *
     * @param request   HTTP 요청 객체
     * @param response  HTTP 응답 객체
     * @param csrfToken CSRF Token
     * @return redirect 할 URL 문자열
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, @RequestAttribute("_csrf") CsrfToken csrfToken) {
        Cookie accessCookie = Arrays.stream(request.getCookies())
                .filter(c -> "accessToken".equals(c.getName()))
                .findFirst()
                .orElse(null);
        Cookie refreshCookie = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .orElse(null);


        if (Objects.nonNull(accessCookie) && !"".equals(accessCookie.getValue())) {
            accessCookie.setMaxAge(0);
            accessCookie.setPath("/");
            accessCookie.setHttpOnly(true);
            response.addCookie(accessCookie);
            redisUtil.setBlackList(accessCookie.getValue(), "access_token", (long) (1000 * 60 * 60));
        }

        if (Objects.nonNull(refreshCookie) && !"".equals(refreshCookie.getValue())) {
            refreshCookie.setMaxAge(0);
            refreshCookie.setPath("/");
            refreshCookie.setHttpOnly(true);
            response.addCookie(refreshCookie);
            userAdapter.doLogout(refreshCookie.getValue(), csrfToken.getToken());
        }

        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/";
    }
}
