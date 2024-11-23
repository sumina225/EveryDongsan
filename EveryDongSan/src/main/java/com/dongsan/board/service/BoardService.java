package com.dongsan.board.service;

import java.util.List;

import com.dongsan.board.model.Board;
import com.dongsan.board.model.Review;

public interface BoardService {
	boolean posts(Board board);

	List<Board> listAll(int page, int size);
	
	List<Board> listAllByStar(int page, int size);

	boolean deleteBoard(int articleNo, String username);

	boolean updateBoard(int articleNo, String username, Board board);

	List<Board> myList(String username, int page, int size);

	List<Board> searchBySchool(String school, int page, int size);

	List<Board> search(String content, int page, int size);

	Board getArticle(int articleNo); // 게시글 상세 조회

	void incrementViewCount(int articleNo); // 조회수 증가
	
	//리뷰
	void addReview(int articleNo, Review review);
    List<Review> getReviews(int articleNo, int page, int size);
    void updateReview(int articleNo, int reviewId, Review review);
    void deleteReview(int articleNo, int reviewId);

	List<Board> studentListAll(int page, int size);
	List<Board> studentSearchBySchool(String school, int page, int size);
	List<Board> studentSearch(String content, int page, int size);
	
	List<Board> rentalListAll(int page, int size);
	List<Board> rentalSearchBySchool(String school, int page, int size);
	List<Board> rentalSearch(String content, int page, int size);
}
