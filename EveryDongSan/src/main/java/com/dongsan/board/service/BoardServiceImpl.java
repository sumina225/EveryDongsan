package com.dongsan.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.board.mapper.BoardMapper;
import com.dongsan.board.mapper.HomeMapper;
import com.dongsan.board.model.Board;
import com.dongsan.home.entity.HomeEntity;
import com.dongsan.home.model.Home;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private HomeMapper homeMapper;

	@Override
	public boolean posts(Board board) {
		HomeEntity homeEntity = board.getHome().toEntity();
		homeEntity.setSchoolId(homeMapper.findSchoolNum(board.getHome().getSchool()));
		// 이름, 멤버id가 중복되는게 있으면 추가 x
		int check = homeMapper.findHome(homeEntity);
		if(check > 0) {
			return false;
		}
		homeMapper.insertHome(homeEntity);
		homeEntity.setNo(homeMapper.getHomeNo(homeEntity));
		
		BoardEntity boardEntity = board.toEntity();
		boardEntity.setHomeNo(homeEntity.getNo());
		
		int n = boardMapper.posts(boardEntity);
		if(n > 0) return true;
		else return false;
	}

	@Override
	public List<Board> listAll() {
		List<BoardEntity> entityList = boardMapper.listAll();
		List<Board> list = new ArrayList<>();
		for(BoardEntity b : entityList) {
			Board temp = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(b.getHomeNo()).get(0);
			Home home = homeEntity.toDto();
			home.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			temp.setHome(home);
			list.add(temp);
		}
		return list;
	}

}
