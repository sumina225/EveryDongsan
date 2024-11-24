package com.dongsan.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.map.service.MapService;

@RestController
@RequestMapping("/map")
public class MapController {
	
	@Autowired
	private MapService mapService;
	
	@GetMapping("/searchBySchool")
	public ResponseEntity<?> searchBySchool(@RequestParam String school){
		return ResponseEntity.ok(mapService.searchByschool(school));
	}
	
	@GetMapping("/searchBoard/{homeNo}")
	public ResponseEntity<?> searchBoardByhomeNo(@PathVariable int homeNo){
		return ResponseEntity.ok(mapService.searchBoardByhomeNo(homeNo));
	}
}
