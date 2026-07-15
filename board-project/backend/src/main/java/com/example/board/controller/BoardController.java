package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.dto.BoardSaveRequest;
import com.example.board.dto.BoardSearchCondition;
import com.example.board.dto.PageResponse;
import com.example.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<PageResponse<Board>> getList(BoardSearchCondition cond) {
        return ResponseEntity.ok(boardService.getList(cond));
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Board> getDetail(@PathVariable("id") Long id) { // 💡 @PathVariable 명시
        return ResponseEntity.ok(boardService.getDetail(id));
    }

    // 게시글 등록 (Form-Data 형태 수신)
    @PostMapping
    public ResponseEntity<Long> create(@Valid @ModelAttribute BoardSaveRequest request) {
        Long id = boardService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    // 🎯 [수정] 게시글 수정 시 파일 업로드가 가능하도록 @RequestBody -> @ModelAttribute로 변경!
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute BoardSaveRequest request
    ) {
        boardService.update(id, request);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}