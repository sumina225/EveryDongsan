package com.dongsan.board.model;

import com.dongsan.board.entity.ReviewEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	private int articleNo; // 리뷰 번호
	private String memberId; // 작성자 ID
	private int homeNo; // 집 번호
	private String content; // 리뷰 내용
	private int reviewScore; // 리뷰 점수

	public ReviewEntity toEntity() {
    	return ReviewEntity.builder()
    			.articleNo(articleNo)
    			.memberId(memberId)
    			.homeNo(homeNo)
    			.content(content)
    			.reviewScore(reviewScore)
    			.build();
    }
}
