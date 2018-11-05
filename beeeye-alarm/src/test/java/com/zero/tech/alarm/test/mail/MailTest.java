package com.zero.tech.alarm.test.mail;

import com.zero.tech.alarm.service.MailService;
import com.zero.tech.base.dto.MailDto;

import java.util.Arrays;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc 发送邮件test
 * @date 2016-09-23 16:52:02
 */
public class MailTest {

    public static void main(String[] args) throws InterruptedException {

        MailDto mailDto = new MailDto();
        mailDto.setTo(Arrays.asList("xxx@xxx.com"));
        mailDto.setContent("报警测试");
        mailDto.setSubject("[SkyEye]-alarm app alert");

        for (int i = 0; i < 100; ++i) {
            new MailService().sendMail(mailDto);
//            Thread.sleep(2000);
        }
    }
}
