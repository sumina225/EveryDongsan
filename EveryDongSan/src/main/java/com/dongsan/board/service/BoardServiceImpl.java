package com.dongsan.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.board.mapper.BoardMapper;
import com.dongsan.board.mapper.HomeMapper;
import com.dongsan.board.model.Board;
import com.dongsan.home.entity.HomeEntity;

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
		homeMapper.insertHome(homeEntity);
		homeEntity.setNo(homeMapper.getHomeNo(homeEntity));
		
		BoardEntity boardEntity = board.toEntity();
		boardEntity.setHomeNo(homeEntity.getNo());
		
		int n = boardMapper.posts(boardEntity);
		if(n > 0) return true;
		else return false;
	}

}
