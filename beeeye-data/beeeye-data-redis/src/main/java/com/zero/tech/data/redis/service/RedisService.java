package com.zero.tech.data.redis.service;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.base.dto.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

import static com.zero.tech.base.constant.RedisKeys.ALARM_EMAIL_QUEUE;

@Component
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public void sendMessage(String info, String mail) {
        this.redisTemplate.opsForList().rightPush(ALARM_EMAIL_QUEUE, this.buildMailDto(info, mail));
    }

    public Optional<MailDto> popFromMailQueue() {
        MailDto mailDto = (MailDto) redisTemplate.opsForList().leftPop(ALARM_EMAIL_QUEUE);
        if (mailDto == null) {
            return Optional.empty();
        } else {
            return Optional.of(mailDto);
        }
    }

    /**
     * 构造mailDto
     *
     * @param info
     * @param mail
     * @return
     */
    private MailDto buildMailDto(String info, String mail) {
        MailDto mailDto = new MailDto();
        mailDto.setTo(Arrays.asList(mail.split(Constants.COMMA)));
        mailDto.setContent(info);
        mailDto.setSubject(Constants.MONITOR_MAIL_SUBJECT);
        return mailDto;
    }
}

