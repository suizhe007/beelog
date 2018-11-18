package com.zero.tech.alarm.service;

import com.zero.tech.alarm.configuration.MailProperties;
import com.zero.tech.base.dto.FileDto;
import com.zero.tech.base.dto.MailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(MailDto mailDto)
            throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(this.mailProperties.getFrom().trim());
        helper.setSubject(mailDto.getSubject());
        helper.setText(mailDto.getContent(), true);
        if (null != mailDto.getTo() && mailDto.getTo().size() > 0) {
            helper.setTo(mailDto.getTo().toArray(new String[mailDto.getTo().size()]));
        }
        if (null != this.mailProperties.getCcs() && this.mailProperties.getCcs().size() > 0) {
            helper.setCc(this.mailProperties.getCcs().toArray(new String[this.mailProperties.getCcs().size()]));
        }
        if (null != mailDto.getFiles() && mailDto.getFiles().size() > 0) {
            for (FileDto file : mailDto.getFiles()) {
                helper.addAttachment(file.getFileName(), new ByteArrayDataSource(file.getFileBytes(), ""));
            }
        }
        javaMailSender.send(mimeMessage);
    }
}
