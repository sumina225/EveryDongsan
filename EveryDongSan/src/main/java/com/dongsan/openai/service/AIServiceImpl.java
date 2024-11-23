package com.dongsan.openai.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.home.entity.HomeEntity;
import com.dongsan.home.model.Home;
import com.dongsan.openai.mapper.AIMapper;

@Service
public class AIServiceImpl implements AIService{
	
	@Autowired
	private AIMapper mapper;

	@Override
	public List<Home> listAll(String school) {
		int schoolId = mapper.findSchoolId(school);
		List<HomeEntity> homeEntity = mapper.listAll(schoolId);
		List<Home> homes = new ArrayList<>();
		for(HomeEntity h : homeEntity) {
			homes.add(h.toDto());
		}
		return homes;
	}

}
