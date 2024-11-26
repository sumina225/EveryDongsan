package com.dongsan.board.entity;

import java.util.List;

import com.dongsan.board.model.Board;
import com.dongsan.board.model.Review;
import com.dongsan.home.entity.HomeEntity;
import com.dongsan.home.model.Home;
import com.dongsan.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {
	private int articleNo;
	private String title;
	private int homeNo;
	private String memberId;
	private String content;
	private int hit = 0;
	
	public Board toDto() {
		return Board.builder()
				.articleNo(articleNo)
				.content(content)
				.title(title)
				.hit(hit)
				.build();	
	}
}
