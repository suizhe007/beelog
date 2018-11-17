package com.zero.tech.alarm.quartz;

import com.alibaba.fastjson.JSON;
import com.zero.tech.alarm.configuration.DingdingProperties;
import com.zero.tech.alarm.configuration.MailProperties;
import com.zero.tech.alarm.configuration.WechatProperties;
import com.zero.tech.alarm.service.DingDingService;
import com.zero.tech.alarm.service.MailService;
import com.zero.tech.alarm.service.WechatService;
import com.zero.tech.base.dto.MailDto;
import com.zero.tech.data.redis.service.RedisService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlarmJob extends AbstractQuartzJob implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmJob.class);
    private ApplicationContext context;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private WechatProperties wechatProperties;
    @Autowired
    private DingDingService dingDingService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private DingdingProperties dingdingProperties;
    @Autowired
    private MailProperties mailProperties;

    @Override
    protected void doExecution(JobExecutionContext jobExecutionContext) {
        try {
            Optional<MailDto> mailDtoOptional = redisService.popFromMailQueue();
            if (mailDtoOptional.isPresent()) {
                LOGGER.info("get a message, {}", JSON.toJSONString(mailDtoOptional.get()));
                // 发送邮件
                if (this.mailProperties.isSwitchFlag()) {
                    this.context.getBean(MailService.class).sendMail(mailDtoOptional.get());
                }

                // 发送微信
                if (this.wechatProperties.isSwitchFlag()) {
                    this.wechatService.send(mailDtoOptional.get().getContent());
                }

                // 发送钉钉
                if (this.dingdingProperties.isSwitchFlag()) {
                    this.dingDingService.send(mailDtoOptional.get().getContent());
                }
            } else {
                LOGGER.info("no message in email queue");
            }
        } catch (Exception e) {
            LOGGER.info("drop a error message", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
