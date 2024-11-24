package com.dongsan.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.main.mapper.MainMapper;

@Service
public class MainServiceImpl implements MainService{

	@Autowired
	private MainMapper mainMapper;
	
	@Override
	public List<Integer> getBoardCount() {
		return mainMapper.getBoardCount();
	}

}
