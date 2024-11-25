package com.dongsan.main.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.main.mapper.MainMapper;

@Service
public class MainServiceImpl implements MainService{

	@Autowired
	private MainMapper mainMapper;
	
	@Override
	public List<Map<String,Object>> getBoardCount() {
		return mainMapper.getBoardCount();
	}

	@Override
	public List<Map<String, Object>> searchKeyword(String keyword) {
		return mainMapper.searchKeyword(keyword);
	}

}
