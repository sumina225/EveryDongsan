package com.dongsan.board.entity;

import com.dongsan.board.model.Review;
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
	private int articleNo; // 리뷰 번호
    private String memberId; // 작성자 ID
    private int homeNo; // 집 번호
    private String content; // 리뷰 내용
    private int reviewScore; // 리뷰 점수
    
    public Review toDto() {
    	return Review.builder()
    			.articleNo(articleNo)
    			.memberId(memberId)
    			.homeNo(homeNo)
    			.content(content)
    			.reviewScore(reviewScore)
    			.build();
    }
}
