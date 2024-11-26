package com.dongsan.map.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.board.entity.BoardEntity;
import com.dongsan.board.mapper.HomeMapper;
import com.dongsan.board.model.Board;
import com.dongsan.home.entity.HomeEntity;
import com.dongsan.home.model.Home;
import com.dongsan.map.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService{
	
	@Autowired
	private MapMapper mapMapper;
	
	@Autowired
	private HomeMapper homeMapper;

	@Override
	public List<Home> searchByschool(String school) {
		int schoolId = mapMapper.searchSchoolId(school);
		List<HomeEntity> entityList = mapMapper.searchBySchool(schoolId);
		List<Home> list = new ArrayList<>();
		for(HomeEntity h : entityList) {
			Home temp = h.toDto();
			temp.setSchool(school);
			list.add(temp);
		}
		return list;
	}

	@Override
	public List<Board> searchBoardByhomeNo(int homeNo) {
		List<BoardEntity> entityList = mapMapper.searchBoardByhomeNo(homeNo);
		List<Board> list = new ArrayList<>();
		for(BoardEntity b : entityList) {
			Board board = b.toDto();
			HomeEntity homeEntity = homeMapper.findHomeByNum(homeNo).get(0);
			Home temp = homeEntity.toDto();
			temp.setSchool(homeMapper.findSchool(homeEntity.getSchoolId()));
			board.setHome(temp);
			list.add(board);
		}
		return list;
	}
	
	
}
