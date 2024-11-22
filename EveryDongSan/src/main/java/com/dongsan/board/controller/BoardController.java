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
import com.dongsan.board.model.Review;
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
	public ResponseEntity<?> listAll(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		return ResponseEntity.ok(boardService.listAll(page, size));
	}
	
	@GetMapping("/listAllByStar")
	public ResponseEntity<?> listAllByStar(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		return ResponseEntity.ok(boardService.listAllByStar(page, size));
	}

	@GetMapping("/{articleNo}")
	public ResponseEntity<?> getArticle(@PathVariable int articleNo) {
		try {
			// 조회수 증가
			boardService.incrementViewCount(articleNo);

			// 게시글 상세 조회
			Board article = boardService.getArticle(articleNo);

			if (article != null) {
				return ResponseEntity.ok(article);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@PostMapping("/{articleNo}/reviews")
	public ResponseEntity<?> addReview(@PathVariable int articleNo, @RequestBody Review review) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		review.setMemberId(username);

		boardService.addReview(articleNo, review);
		return ResponseEntity.status(HttpStatus.CREATED).body("리뷰가 등록되었습니다.");
	}

	// 2. 특정 매물의 모든 리뷰 조회
	@GetMapping("/{articleNo}/reviews")
	public ResponseEntity<?> getReviews(@PathVariable int articleNo, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		return ResponseEntity.ok(boardService.getReviews(articleNo, page, size));
	}

	// 3. 특정 리뷰 수정
	@PutMapping("/{articleNo}/reviews/{reviewId}")
	public ResponseEntity<?> updateReview(@PathVariable int articleNo, @PathVariable int reviewId,
			@RequestBody Review review) {
		boardService.updateReview(articleNo, reviewId, review);
		return ResponseEntity.ok("리뷰가 수정되었습니다.");
	}

	// 4. 특정 리뷰 삭제
	@DeleteMapping("/{articleNo}/reviews/{reviewId}")
	public ResponseEntity<?> deleteReview(@PathVariable int articleNo, @PathVariable int reviewId) {
		boardService.deleteReview(articleNo, reviewId);
		return ResponseEntity.ok("리뷰가 삭제되었습니다.");
	}

	@DeleteMapping("/{articleNo}/delete")
	public ResponseEntity<?> deleteBoard(@PathVariable int articleNo) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (boardService.deleteBoard(articleNo, username)) {
			return ResponseEntity.ok("삭제되었습니다.");
		} else {
			return ResponseEntity.ok("삭제권한이 없습니다");
		}

	}

	@PutMapping("/{articleNo}/update")
	public ResponseEntity<?> updateBoard(@PathVariable int articleNo, @RequestBody Board board) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (boardService.updateBoard(articleNo, username, board)) {
			return ResponseEntity.ok("수정되었습니다.");
		} else {
			return ResponseEntity.ok("수정권한이 없습니다");
		}
	}

	// 본인이 올린글 보기
	@GetMapping("/myList")
	public ResponseEntity<?> myList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return ResponseEntity.ok(boardService.myList(username, page, size));
	}

	@GetMapping("/searchBySchool")
	public ResponseEntity<?> searchBySchool(@RequestParam String school, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		return ResponseEntity.ok(boardService.searchBySchool(school, page, size));
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam String content, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		return ResponseEntity.ok(boardService.search(content, page, size));
	}

}
