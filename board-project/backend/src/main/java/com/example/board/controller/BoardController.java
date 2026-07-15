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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

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
    public ResponseEntity<Board> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getDetail(id));
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody BoardSaveRequest request) {
        Long id = boardService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody BoardSaveRequest request) {
        boardService.update(id, request);
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
