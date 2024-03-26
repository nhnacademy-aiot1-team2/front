package com.nhnacademy.front.controller;

import com.nhnacademy.front.adaptor.LoginAdaptor;
import com.nhnacademy.front.dto.AccessTokenResponse;
import com.nhnacademy.front.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginAdaptor loginAdaptor;
    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, Model model){
        AccessTokenResponse token = loginAdaptor.doLogin(loginRequest);
        model.addAttribute("token", token);

        return "redirect:/";
    }
}
