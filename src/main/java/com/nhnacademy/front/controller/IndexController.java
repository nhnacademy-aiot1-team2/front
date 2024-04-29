package com.nhnacademy.front.controller;

import com.nhnacademy.front.utils.AccessTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("accessToken_temp", AccessTokenUtil.findAccessTokenInRequest(request));

        return "index";
    }
}