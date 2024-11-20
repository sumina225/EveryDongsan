package com.dongsan.board.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.home.model.Home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	private int articleNo; // 게시글 번호
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private int hit; // 조회수
    private Home home; // 매물 정보 (HomeDTO)
    
    public BoardEntity toEntity() {
    	return BoardEntity.builder()
    			.articleNo(articleNo)
    			.content(content)
    			.title(title)
    			.hit(hit)
    			.homeNo(home.getNo())
    			.memberId(home.getMemberId())
    			.build();
    	
    	
    }
}
