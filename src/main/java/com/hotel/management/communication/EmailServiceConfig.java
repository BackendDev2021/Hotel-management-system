package com.hotel.management.communication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.hotel.management.utils.Constants;

@Component
public class EmailServiceConfig {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendEmailForRegistration(Mail mail) throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariables(mail.getProps());
		String html = templateEngine.process("welcome-mail.html", context);
		helper.setFrom(Constants.TEST_MAIL);
		helper.setTo(mail.getMailTo());
		helper.setText(html, true);
		helper.setSubject(Constants.ACTIVATE_ACCOUNT);
		emailSender.send(message);
	}

	public void sendEmailForForgetPassword(Mail mail) throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper2 = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariables(mail.getProps());
		String html = templateEngine.process("temp-password.html", context);
		helper2.setFrom(Constants.TEST_MAIL);
		helper2.setTo(mail.getMailTo());
		helper2.setText(html, true);
		helper2.setSubject(Constants.RESET_PASSWORD);
		emailSender.send(message);

	}

}
