package com.dongsan.member.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dongsan.member.entity.MemberEntity;
import com.dongsan.member.model.Member;

@Mapper
public interface MemberMapper {
	MemberEntity userInfo(String id) throws SQLException; //자신정보확인
	void memberUpdate(MemberEntity memberEntity) throws SQLException; // 정보수정
	void signup(MemberEntity memberEntity) throws SQLException; // 회원가입
	void memberDelete(String id) throws SQLException; // 회원탈퇴
	
	
	//emailDomain 선택
	int findEmailDomainNum(String emailDomain);
	String findEmailDomain(int emailDomainId);
	void insertEmailDomain(String emailDomain);

	//school 선택
	int findSchoolNum(String school);
	String findSchool(int schoolId);
	
	//인증
	
	MemberEntity findById(String id); //로그인
	
	// 아이디,비밀번호 찾기
	String findId(Map<String,Object> map);
	void updatePassword(Map<String,Object> map);
}
