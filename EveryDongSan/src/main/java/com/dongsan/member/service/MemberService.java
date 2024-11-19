package com.dongsan.member.service;

import java.sql.SQLException;

import com.dongsan.member.entity.MemberEntity;
import com.dongsan.member.model.Member;

public interface MemberService {
	Member login(String id,String password) throws Exception;
	Member userInfo(String userId) throws Exception;
	void memberUpdate(Member member) throws Exception; // 정보수정
	void signup(Member member) throws Exception; // 회원가입
	void memberDelete(String userId) throws Exception; // 회원탈퇴
	Member toDto(MemberEntity memberEntity);
	String findId(String emailId, String emailDomain);
}
