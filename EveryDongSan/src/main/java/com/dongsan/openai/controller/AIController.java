package com.dongsan.openai.controller;

import java.util.List;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.board.model.Board;
import com.dongsan.board.service.BoardService;
import com.dongsan.home.model.Home;
import com.dongsan.openai.service.AIService;
import com.dongsan.openai.util.PromptTemplateLoader;

@RestController
public class AIController {

	@Autowired
	private ChatModel chatModel;

	@Autowired
	private AIService aiService;

	@Autowired
	private PromptTemplateLoader promptLoader;

	@GetMapping("/recommend")
	public ResponseEntity<?> getAttractions(@RequestParam int price, @RequestParam String school) {
		try {
			List<Home> homes = aiService.listAll(school);
			StringBuilder homesInfo = new StringBuilder();
			for(Home h : homes) {
				homesInfo.append(h.toString()).append("\n");
			}
			String userPromptTemplate = promptLoader.loadUserPrompt();
			PromptTemplate userTemplate = new PromptTemplate(userPromptTemplate);
			userTemplate.add("price", price);
			userTemplate.add("school", school);
			userTemplate.add("homes", homesInfo);
			String userCommand = userTemplate.render();

			// 시스템 프롬프트 로드
			String systemPromptTemplate = promptLoader.loadSystemPrompt();
			PromptTemplate systemTemplate = new PromptTemplate(systemPromptTemplate);

			String systemCommand = systemTemplate.render();

			// 메시지 생성
			Message userMessage = new UserMessage(userCommand);
			Message systemMessage = new SystemMessage(systemCommand);

			// API 호출
			String response = chatModel.call(userMessage, systemMessage);
			System.out.println(response);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing request: " + e.getMessage());
		}
	}
}
