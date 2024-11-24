package com.dongsan.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.map.service.MapService;

@RestController
@RequestMapping("/map")
public class MapController {
	
	@Autowired
	private MapService mapService;
	
	
}
