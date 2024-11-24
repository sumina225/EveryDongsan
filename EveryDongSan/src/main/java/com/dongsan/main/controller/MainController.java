package com.dongsan.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.main.service.MainService;
import com.dongsan.map.service.MapService;

@RestController
@RequestMapping("/main")
public class MainController {
	
	@Autowired
	private MainService mainService;
	
	@GetMapping("/getBoardCount")
	public ResponseEntity<?> getBoardCount(){
		return ResponseEntity.ok(mainService.getBoardCount());
		// 리스트를 학교와 카운트가 담겨있는걸로 수정
	}
}
