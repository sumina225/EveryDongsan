package com.dongsan.member.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
	private String id;
	private String pw;
	private String name;
	private String emailId;
	private int emailDomainId;
	private String tel;
	private String role;
	private int schoolId;
}
