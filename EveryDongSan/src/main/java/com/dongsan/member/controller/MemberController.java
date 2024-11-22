package com.dongsan.member.controller;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.mail.model.Mail;
import com.dongsan.mail.service.MailService;
import com.dongsan.member.entity.MemberEntity;
import com.dongsan.member.model.Member;
import com.dongsan.member.service.MemberService;
import com.dongsan.security.JwtTokenBlacklistService;
import com.dongsan.security.JwtTokenUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MailService mailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtTokenBlacklistService jwtTokenBlacklistService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody Member member) {
		try {
			memberService.signup(member);
			return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
		} catch (DuplicateKeyException e) {
			// 중복된 이메일이나 사용자 이름이 있을 때
			return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
		} catch (InvalidInputException e) {
			// 입력값이 잘못된 경우
			return new ResponseEntity<>("입력값이 잘못되었습니다.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// 일반적인 예외 처리
			e.printStackTrace();
			return new ResponseEntity<>("회원가입 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Member memberInfo, HttpServletResponse response) {
		String id = memberInfo.getId();
		String password = memberInfo.getPassword();
		try {
			Member member = memberService.login(id, password);
			if (member != null) {
				String token = jwtTokenUtil.generateToken(member);
				response.setHeader("Authorization", "Bearer " + token);

				return ResponseEntity.ok(token);
			} else {
				return new ResponseEntity<>("잘못된 로그인 정보입니다.", HttpStatus.UNAUTHORIZED);
			}
		} catch (UsernameNotFoundException e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalArgumentException e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("처리 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			jwtTokenBlacklistService.addTokenToBlacklist(token);
		}
		return new ResponseEntity<>("로그아웃되었습니다.", HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<?> userInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); // 인증된 사용자 이름 추출
		try {
			Member member = memberService.userInfo(username);
			return ResponseEntity.ok(member); // 사용자 정보를 반환
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("처리 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> memberDelete(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); // 인증된 사용자 이름 추출
		String authorizationHeader = request.getHeader("Authorization");

		// JWT 토큰 추출
		String token = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7); // Bearer 뒤의 토큰 부분 추출
		}

		try {
			// 회원 삭제
			memberService.memberDelete(username);

			// JWT 토큰 블랙리스트에 추가
			if (token != null) {
				jwtTokenBlacklistService.addTokenToBlacklist(token); // 토큰을 블랙리스트에 추가
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("회원 삭제 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// 성공적으로 삭제되었을 때
		return new ResponseEntity<>("회원이 삭제되었습니다.", HttpStatus.NO_CONTENT);
	}

	@PutMapping("/update")
	public ResponseEntity<String> memberUpdate(@RequestParam String emailId, @RequestParam String emailDomain,
			@RequestParam String pw, @RequestParam String tel, @RequestParam String school,
			HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization").substring(7); // 토큰 추출
			Member member = new Member();
			member.setId(jwtTokenUtil.extractUsername(token));
			member.setEmailId(emailId);
			member.setEmailDomain(emailDomain);
			member.setPassword(pw);
			member.setTel(tel);
			member.setSchool(school);
			System.out.println(member);
			memberService.memberUpdate(member);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>("회원정보가 업데이트되었습니다.", HttpStatus.OK);
	}

	@GetMapping("/findId")
	public ResponseEntity<String> findId(@RequestParam String emailId, @RequestParam String emailDomain) {
		String memberId = memberService.findId(emailId, emailDomain);
		if (memberId != null) {
			return ResponseEntity.ok(memberId);
		} else {
			return new ResponseEntity<>("해당 이메일로 생성된 계정이 존재하지 않습니다.", HttpStatus.NO_CONTENT);
		}

	}

	@ResponseBody
	@PostMapping("/findPw") // 이 부분은 각자 바꿔주시면 됩니다.
	public String emailCheck(@RequestBody Mail mailDTO) throws MessagingException, UnsupportedEncodingException {
		String authCode = mailService.sendSimpleMessage(mailDTO.getEmail());
		return authCode; // Response body에 값을 반환
	}

}
