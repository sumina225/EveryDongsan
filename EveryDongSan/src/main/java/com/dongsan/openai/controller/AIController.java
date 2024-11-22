package com.dongsan.openai.controller;

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

import com.dongsan.openai.util.PromptTemplateLoader;

@RestController
public class AIController {

	@Autowired
	private ChatModel chatModel;
	
	@Autowired
	private PromptTemplateLoader promptLoader;
	
	@GetMapping("/recommend")
	public ResponseEntity<?> getAttractions(@RequestParam int price, @RequestParam String school){
		try {
		String userPromptTemplate = promptLoader.loadUserPrompt();
        PromptTemplate userTemplate = new PromptTemplate(userPromptTemplate);
        userTemplate.add("price", price);
        userTemplate.add("school", school);
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
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error processing request: " + e.getMessage());
		}
	}
}
