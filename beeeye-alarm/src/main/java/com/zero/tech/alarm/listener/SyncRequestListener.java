package com.zero.tech.alarm.listener;

import com.alibaba.fastjson.JSON;
import com.zero.tech.alarm.configuration.dingding.DingdingProperties;
import com.zero.tech.alarm.configuration.mail.MailProperties;
import com.zero.tech.alarm.configuration.wechat.WechatProperties;
import com.zero.tech.alarm.service.DingDingService;
import com.zero.tech.alarm.service.MailService;
import com.zero.tech.alarm.service.WechatService;
import com.zero.tech.base.dto.MailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc 数据进入队列的监听器，负责处理
 * @date 2017-09-29 09:16:16
 * Spring容器会检测容器中的所有Bean，如果发现某个Bean实现了ApplicationContextAware接口，
 * Spring容器会在创建该Bean之后，自动调用该Bean的setApplicationContextAware()方法，
 * 调用该方法时，会将容器本身作为参数传给该方法——
 * 该方法中的实现部分将Spring传入的参数（容器本身）赋给该类对象的applicationContext实例变量，
 * 因此接下来可以通过该applicationContext实例变量来访问容器本身。
 *
 */
@Component
public class SyncRequestListener implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncRequestListener.class);

    private ApplicationContext context;

    @Autowired
    private WechatService wechatService;
    @Autowired
    private WechatProperties wechatProperties;
    @Autowired
    private DingDingService dingDingService;
    @Autowired
    private DingdingProperties dingdingProperties;
    @Autowired
    private MailProperties mailProperties;

    public void onMessage(Object object) {
        MailDto mailDto = null;
        try {
            if (null != object) {
                mailDto = (MailDto) object;
            }
            LOGGER.info("get a message, {}", JSON.toJSONString(object));

            // 发送邮件
            if (this.mailProperties.isSwitchFlag()) {
                this.context.getBean(MailService.class).sendMail(mailDto);
            }

            // 发送微信
            if (this.wechatProperties.isSwitchFlag()) {
                this.wechatService.send(mailDto.getContent());
            }

            // 发送钉钉
            if (this.dingdingProperties.isSwitchFlag()) {
                this.dingDingService.send(mailDto.getContent());
            }

        }  catch (Exception e) {
            LOGGER.info("drop a error message, {}, {}", mailDto.getContent(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
