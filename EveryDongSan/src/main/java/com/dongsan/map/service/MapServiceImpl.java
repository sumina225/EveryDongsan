package com.dongsan.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsan.map.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService{
	
	@Autowired
	private MapMapper mapMapper;
	
	
}
