package com.dongsan.board.controller;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.board.model.Board;
import com.dongsan.board.service.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@PostMapping("/posts")
	public ResponseEntity<?> posts(@RequestBody Board board) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		board.getHome().setMemberId(username);
		return ResponseEntity.ok(boardService.posts(board));
	}
	
	@GetMapping("/listAll")
	public ResponseEntity<?> listAll(){
		return ResponseEntity.ok(boardService.listAll());
	}
	
	@DeleteMapping("/{articleNo}/delete")
	public ResponseEntity<?> deleteBoard(@PathVariable int articleNo){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(boardService.deleteBoard(articleNo,username)) {
			return ResponseEntity.ok("삭제되었습니다.");
		}else {
			return ResponseEntity.ok("삭제권한이 없습니다");
		}
		
	}
	
	@PutMapping("/{articleNo}/update")
	public ResponseEntity<?> updateBoard(@PathVariable int articleNo, @RequestBody Board board){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(boardService.updateBoard(articleNo,username,board)) {
			return ResponseEntity.ok("수정되었습니다.");
		}else {
			return ResponseEntity.ok("수정권한이 없습니다");
		}
	}
	// 본인이 올린글 보기
	@GetMapping("/myList")
	public ResponseEntity<?> myList(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return ResponseEntity.ok(boardService.myList(username));
	}
	
	@GetMapping("/searchBySchool")
	public ResponseEntity<?> searchBySchool(@RequestParam String school){
		return ResponseEntity.ok(boardService.searchBySchool(school));
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam String content){
		return ResponseEntity.ok(boardService.search(content));
	}
	
	

}
