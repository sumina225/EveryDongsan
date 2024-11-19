package com.dongsan.mail.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dongsan.member.mapper.MemberMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	private static final String senderEmail = "psm5927@gmail.com";

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String createNumber() {
		Random random = new Random();
		StringBuilder key = new StringBuilder();

		for (int i = 0; i < 8; i++) { // 인증 코드 8자리
			int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

			switch (index) {
			case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
			case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
			case 2 -> key.append(random.nextInt(10)); // 숫자
			}
		}
		return key.toString();
	}

	@Override
	public MimeMessage createMail(String mail, String number) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();

		message.setFrom(senderEmail);
		message.setRecipients(MimeMessage.RecipientType.TO, mail);
		message.setSubject("에브리동산 비밀번호 찾기");
		String body = "";
		body += "<h3>임시 비밀번호입니다.</h3>";
		body += "<h1>" + number + "</h1>";
		body += "<h3>감사합니다.</h3>";
		message.setText(body, "UTF-8", "html");

		return message;
	}

	@Override
	public String sendSimpleMessage(String sendEmail) throws MessagingException {
		String number = createNumber(); // 랜덤 인증번호 생성

		String[] emailParts = sendEmail.split("@");
		if (emailParts.length != 2) {
			throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
		}
		String emailId = emailParts[0];
		String emailDomain = emailParts[1];
		
		int emailDomainId = memberMapper.findEmailDomainNum(emailDomain);
		if (emailDomainId == 0) {
			memberMapper.insertEmailDomain(emailDomain);
			emailDomainId = memberMapper.findEmailDomainNum(emailDomain);
		}

		MimeMessage message = createMail(sendEmail, number); // 메일 생성
		try {
			javaMailSender.send(message); // 메일 발송

			String encodedPassword = bCryptPasswordEncoder.encode(number);
			Map<String,Object> map = new HashMap<>();
			map.put("emailId", emailId);
			map.put("emailDomainId", emailDomainId);
			map.put("pw", encodedPassword);
			memberMapper.updatePassword(map);

		} catch (MailException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("메일 발송 중 오류가 발생했습니다.");
		}

		return number; // 생성된 인증번호 반환
	}

}
