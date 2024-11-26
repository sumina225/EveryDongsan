package com.dongsan.parsing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.parsing.service.ParsingService;

@RestController
@RequestMapping("/parsing")
public class ParsingController {
	
	@Autowired
	private ParsingService parsingService;
	
	@GetMapping("/parse")
	public ResponseEntity<?> parseFile(){
		String fileName = "매물.xlsx";
		try {
			parsingService.parseAndSaveRealEstateData(fileName);
			return ResponseEntity.ok("파일 파싱 및 저장 완료");
		} catch (Exception e) {
			e.printStackTrace();
            return ResponseEntity.ok("파일 파싱 중 오류 발생");
		}
	}
}
