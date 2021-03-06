package com.divatt.auth.services;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.divatt.auth.exception.CustomException;

@Service
public class MailService {
	@Autowired
	JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String body,Boolean enableHtml) {

//		SimpleMailMessage message = new SimpleMailMessage();
//
//		message.setFrom("ulearn@co.in");
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(body);
//		mailSender.send(message);
		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setSubject(subject);
			helper.setFrom("soumen.dolui@nitsolution.in");
			helper.setTo(to);
			helper.setText(body, enableHtml);
//			helper.addAttachment(body, null);
			mailSender.send(message);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}
	
	public void sendEmailWithAttachment(String to, String subject, String body,Boolean enableHtml,File file) {

//		SimpleMailMessage message = new SimpleMailMessage();
//
//		message.setFrom("ulearn@co.in");
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(body);
//		mailSender.send(message);
		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			helper.setSubject(subject);
			helper.setFrom("soumen.dolui@nitsolution.in");
			helper.setTo(to);
			helper.setText(body, enableHtml);
			helper.addAttachment("Invoice", file);
			mailSender.send(message);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

	}
}
