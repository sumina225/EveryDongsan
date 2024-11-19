package com.dongsan.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {
	public String createNumber();
	public MimeMessage createMail(String mail, String number) throws MessagingException;
	public String sendSimpleMessage(String sendEmail) throws MessagingException;
}
