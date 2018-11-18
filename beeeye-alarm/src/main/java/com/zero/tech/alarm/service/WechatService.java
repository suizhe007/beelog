package com.zero.tech.alarm.service;

import com.zero.tech.alarm.configuration.WechatProperties;
import com.zero.tech.data.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatService.class);

    @Autowired
    private WechatProperties wechatProperties;

    public void send(String msg) {
        String reponse = HttpRequest.get(this.wechatProperties.getUrl(), msg);
        if (reponse != null) {
            // 请求成功
            LOGGER.info("发送微信成功");
        } else {
            LOGGER.info("发送微信失败");
        }
    }
}
