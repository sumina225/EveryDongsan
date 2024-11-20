package com.dongsan.board.model;

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
    private String content; // 리뷰 내용
    private int reviewScore; // 리뷰 점수
    private int hit; // 조회수
    private String reviewerName; // 리뷰 작성자 이름
}
