package com.dongsan.member.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dongsan.member.entity.MemberEntity;
import com.dongsan.member.mapper.MemberMapper;
import com.dongsan.member.model.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Member login(String id, String password) throws Exception {
		MemberEntity memberEntityInfo = memberMapper.findById(id);
		if (memberEntityInfo == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		System.out.println(memberEntityInfo);

		// 비밀번호 검증
		if (!bCryptPasswordEncoder.matches(password, memberEntityInfo.getPw())) {
			System.out.println(password + "\t" + memberEntityInfo.getPw());
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		Member memberInfo = toDto(memberEntityInfo);
		return memberInfo;
	}

	@Override
	public Member userInfo(String userId) throws Exception {
		MemberEntity memberEntity = memberMapper.userInfo(userId);
		if (memberEntity == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		Member member = toDto(memberEntity);
		return member;
		
	}

	@Override
	public void memberUpdate(Member member) throws Exception {
		MemberEntity memberEntity = new MemberEntity();
		int emailDomainId = memberMapper.findEmailDomainNum(member.getEmailDomain());
		if (emailDomainId == 0) {
			memberMapper.insertEmailDomain(member.getEmailDomain());
			emailDomainId = memberMapper.findEmailDomainNum(member.getEmailDomain());
		}
		int schoolId = memberMapper.findSchoolNum(member.getSchool());
		String encodedPassword = bCryptPasswordEncoder.encode(member.getPassword());
		memberEntity.setPw(encodedPassword);
		memberEntity.setEmailDomainId(emailDomainId);
		memberEntity.setSchoolId(schoolId);
		memberEntity.setEmailId(member.getEmailId());
		memberEntity.setId(member.getId());
		memberEntity.setName(member.getName());
		memberEntity.setRole(member.getRole());
		memberEntity.setTel(member.getTel());
		memberMapper.memberUpdate(memberEntity);

	}

	@Override
	public void signup(Member member) throws Exception {
		if (member.getId() == null || member.getPassword() == null) {
			throw new InvalidInputException();
		}
		MemberEntity memberEntity = new MemberEntity();
		int emailDomainId = memberMapper.findEmailDomainNum(member.getEmailDomain());
		if (emailDomainId == 0) {
			memberMapper.insertEmailDomain(member.getEmailDomain());
			emailDomainId = memberMapper.findEmailDomainNum(member.getEmailDomain());
		}
		int schoolId = memberMapper.findSchoolNum(member.getSchool());

		String encodedPassword = bCryptPasswordEncoder.encode(member.getPassword());
		memberEntity.setPw(encodedPassword);
		memberEntity.setEmailDomainId(emailDomainId);
		memberEntity.setSchoolId(schoolId);
		memberEntity.setEmailId(member.getEmailId());
		memberEntity.setId(member.getId());
		memberEntity.setName(member.getName());
		memberEntity.setRole(member.getRole());
		memberEntity.setTel(member.getTel());

		memberMapper.signup(memberEntity);

	}

	@Override
	public void memberDelete(String userId) throws Exception {
		memberMapper.memberDelete(userId);

	}

	@Override
	public Member toDto(MemberEntity memberEntity) {
		Member member = new Member();
		member.setSchool(memberMapper.findSchool(memberEntity.getSchoolId()));
		member.setEmailDomain(memberMapper.findEmailDomain(memberEntity.getEmailDomainId()));
		member.setEmailId(memberEntity.getEmailId());
		member.setId(memberEntity.getId());
		member.setName(memberEntity.getName());
		member.setPassword(null);
		member.setRole(memberEntity.getRole());
		member.setTel(memberEntity.getTel());
		return member;
	}

	@Override
	public String findId(String emailId, String emailDomain) {
		int emailDomainId = memberMapper.findEmailDomainNum(emailDomain);
		Map<String,Object> map = new HashMap<>();
		map.put("emailId", emailId);
		map.put("emailDomainId", emailDomainId);
		return memberMapper.findId(map);
	}

}
