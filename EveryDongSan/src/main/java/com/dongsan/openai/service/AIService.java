package com.dongsan.openai.service;

import java.util.List;

import com.dongsan.home.model.Home;

public interface AIService {
	List<Home> listAll(String school); 
}
