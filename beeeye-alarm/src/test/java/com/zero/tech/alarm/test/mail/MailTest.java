package com.zero.tech.alarm.test.mail;

import com.zero.tech.data.redis.service.RedisService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MailTest extends BaseTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void testSendMail() throws Exception {
        for (int i = 0; i < 2; ++i) {
            if (redisService != null) {
                redisService.sendMessage("报警测试" + i, "qr7972@163.com");
            }
        }
    }
}
