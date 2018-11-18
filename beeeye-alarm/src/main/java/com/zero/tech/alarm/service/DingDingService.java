package com.zero.tech.alarm.service;

import com.alibaba.fastjson.JSON;
import com.zero.tech.alarm.configuration.DingdingProperties;
import com.zero.tech.alarm.dto.DingDingDto;
import com.zero.tech.data.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DingDingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatService.class);

    @Autowired
    private DingdingProperties dingdingProperties;

    public void send(String msg) {
        DingDingDto dingDingDto = new DingDingDto();
        dingDingDto.setAt(new DingDingDto.At(true)).setText(new DingDingDto.Text(msg));
        String reponse = HttpRequest.post(this.dingdingProperties.getUrl(), JSON.toJSONString(dingDingDto));
        if (reponse != null) {
            // 请求成功
            LOGGER.info("发送钉钉成功");
        } else {
            LOGGER.info("发送钉钉失败");
        }
    }

}
