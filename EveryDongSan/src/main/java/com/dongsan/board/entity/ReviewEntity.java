package com.dongsan.board.entity;

import com.dongsan.home.entity.HomeEntity;
import com.dongsan.member.entity.MemberEntity;
import com.dongsan.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {
	private int articleNo;
	private String title;
	private String content;
	private int reviewScore;
	private int hit = 0;
	
	
	private MemberEntity member;
	private HomeEntity home;
	
}
