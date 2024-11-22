package com.dongsan.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.board.entity.ReviewEntity;
import com.dongsan.home.entity.HomeEntity;

@Mapper
public interface BoardMapper {
	int posts(BoardEntity board);
	List<BoardEntity> listAll(@Param("offset") int offset, @Param("limit") int limit);
	List<BoardEntity> listAllByStar(@Param("offset") int offset, @Param("limit") int limit);
	List<BoardEntity> myList(@Param("memberId") String username, @Param("offset") int offset, @Param("limit") int limit);
	List<BoardEntity> searchBySchoolId(int schoolId, @Param("offset") int offset, @Param("limit") int limit);
	List<BoardEntity> search(@Param("content") String content, @Param("offset") int offset, @Param("limit") int limit);
	
	void deleteBoard(int articleNo);
	void updateBoard(BoardEntity boardEntity);
	
	
	String findMemberIdByBoard(int articleNo);
	int findHomeNoByBoard(int articleNo);
	
	int findBoard(BoardEntity board);
	
    BoardEntity getArticle(@Param("articleNo") int articleNo); // 게시글 상세 조회
    void incrementViewCount(@Param("articleNo") int articleNo); // 조회수 증가
    
    void insertReview(ReviewEntity review);
    List<ReviewEntity> selectReviewsByHomeNo(@Param("homeNo") int homeNo, @Param("offset") int offset, @Param("limit") int limit);
    void updateReview(ReviewEntity review);
    void deleteReview(@Param("reviewId") int reviewId);
}
