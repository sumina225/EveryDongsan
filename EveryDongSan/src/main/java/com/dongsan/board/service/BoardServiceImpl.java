package com.dongsan.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.board.entity.ReviewEntity;
import com.dongsan.board.mapper.BoardMapper;
import com.dongsan.board.mapper.HomeMapper;
import com.dongsan.board.model.Board;
import com.dongsan.board.model.Review;
import com.dongsan.home.entity.HomeEntity;
import com.dongsan.home.model.Home;
import com.dongsan.member.mapper.MemberMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private HomeMapper homeMapper;
	
	@Autowired
	private MemberMapper memberMapper;

	@Override
	public boolean posts(Board board) {
		HomeEntity homeEntity = board.getHome().toEntity();
		homeEntity.setSchoolId(homeMapper.findSchoolNum(board.getHome().getSchool()));
		// 이름, 멤버id가 중복되는게 있으면 추가 x
		int check = homeMapper.findHome(homeEntity);
		BoardEntity boardEntity = board.toEntity();
		if(boardMapper.findBoard(boardEntity) > 0) {
			return false;
		}
		if (check == 0) {
			homeMapper.insertHome(homeEntity);
		}
		homeEntity.setNo(homeMapper.getHomeNo(homeEntity).get(0));
		
		
		boardEntity.setHomeNo(homeEntity.getNo());
		int n = boardMapper.posts(boardEntity);
		if (n > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<Board> listAll(int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.listAll(offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			
			list.add(temp);
		}
		return list;
	}
	
	@Override
	public List<Board> listAllByStar(int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.listAllByStar(offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public boolean deleteBoard(int articleNo, String username) {
		if(username.equals(boardMapper.findMemberIdByBoard(articleNo))) {
			boardMapper.deleteBoard(articleNo);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateBoard(int articleNo, String username,Board board) {
		if(username.equals(boardMapper.findMemberIdByBoard(articleNo))) {
			System.out.println(board);
			board.setArticleNo(articleNo);
			// find
			
			HomeEntity homeEntity = board.getHome().toEntity();
			homeEntity.setNo(boardMapper.findHomeNoByBoard(articleNo));
			homeEntity.setSchoolId(homeMapper.findSchoolNum(board.getHome().getSchool()));
			System.out.println(homeEntity);
			homeMapper.updateHome(homeEntity);
			
			BoardEntity boardEntity = board.toEntity();
			boardMapper.updateBoard(boardEntity);
			return true;
		}
		return false;
	}

	@Override
	public List<Board> myList(String username, int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.myList(username,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> searchBySchool(String school, int page, int size) {
		int offset = (page-1) * size;
		int schoolId = homeMapper.findSchoolNum(school);
		List<BoardEntity> entityList = boardMapper.searchBySchoolId(schoolId,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> search(String content, int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.search(content,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public Board getArticle(int articleNo) {
		BoardEntity boardEntity = boardMapper.getArticle(articleNo);
		Board temp = boardEntity.toDto();
		HomeEntity homeEntity = homeMapper.findHomeByNum(boardEntity.getHomeNo()).get(0);
		String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
		temp.setTel(tel);
		Home home = homeEntity.toDto();
		home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
		temp.setHome(home);
		return temp;
	}

	@Override
	public void incrementViewCount(int articleNo) {
		 boardMapper.incrementViewCount(articleNo);
	}

	@Override
	public void addReview(int articleNo, Review review) {
		ReviewEntity reviewEntity = review.toEntity();
		System.out.println(articleNo);
		reviewEntity.setHomeNo(boardMapper.findHomeNoByBoard(articleNo));
		boardMapper.insertReview(reviewEntity);
		
		
	}

	@Override
	public List<Review> getReviews(int articleNo, int page, int size) {
		int offset = (page-1) * size;
		System.out.println(offset + " " + page + " " + size);
		int homeNo = boardMapper.findHomeNoByBoard(articleNo);
		System.out.println(homeNo);
		List<ReviewEntity> entityList = boardMapper.selectReviewsByHomeNo(homeNo,offset,size);
		List<Review> list = new ArrayList<>();
		int count = 1;
		System.out.println(entityList);
		for(ReviewEntity r : entityList) {
			Review temp = r.toDto();
//			temp.setNo(count++);
			list.add(temp);
		}
		return list;
	}

	@Override
	public void updateReview(int articleNo, int reviewId, Review review) {
		ReviewEntity reviewEntity = review.toEntity();
		reviewEntity.setArticleNo(reviewId);
		boardMapper.updateReview(reviewEntity);
		
	}

	@Override
	public void deleteReview(int articleNo, int reviewId) {
		boardMapper.deleteReview(reviewId);
		
	}

	@Override
	public List<Board> studentListAll(int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.studentListAll(offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> studentSearchBySchool(String school, int page, int size) {
		int offset = (page-1) * size;
		int schoolId = homeMapper.findSchoolNum(school);
		List<BoardEntity> entityList = boardMapper.studentSearchBySchoolId(schoolId,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> studentSearch(String content, int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.studentSearch(content,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> rentalListAll(int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.rentalListAll(offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> rentalSearchBySchool(String school, int page, int size) {
		int offset = (page-1) * size;
		int schoolId = homeMapper.findSchoolNum(school);
		List<BoardEntity> entityList = boardMapper.rentalSearchBySchoolId(schoolId,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> rentalSearch(String content, int page, int size) {
		int offset = (page-1) * size;
		List<BoardEntity> entityList = boardMapper.rentalSearch(content,offset,size);
		List<Board> list = new ArrayList<>();
		for (BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			String tel = memberMapper.findById(homeEntity.getMemberId()).getTel();
			temp.setTel(tel);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

	
}
