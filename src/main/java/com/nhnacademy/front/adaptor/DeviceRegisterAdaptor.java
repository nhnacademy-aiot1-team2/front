package com.nhnacademy.front.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "rule-engine", url = "${gateway.url}", path = "/api/rule")
public interface DeviceRegisterAdaptor {

    @PostMapping(value = "/device/register")
    void sendDeviceInfo(@RequestHeader("Authorization") String accessToken, @RequestBody String deviceRegisterInfo);
}
