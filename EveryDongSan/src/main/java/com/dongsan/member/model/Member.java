package com.dongsan.member.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@NotBlank(message = "아이디는 필수입니다.")
	private String id;
	@NotBlank(message = "비밀번호는 필수입니다.")
	private String password;
	
	private String name;
	private String emailId;
	private String emailDomain;
	private String tel;
	private String role;
	private String school;
}
