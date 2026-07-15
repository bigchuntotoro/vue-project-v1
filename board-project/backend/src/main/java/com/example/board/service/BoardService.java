package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.dto.BoardSaveRequest;
import com.example.board.dto.BoardSearchCondition;
import com.example.board.dto.PageResponse;

public interface BoardService {

    // 게시글 목록 조회
    PageResponse<Board> getList(BoardSearchCondition cond);

    // 게시글 상세 조회 (조회수 증가 및 조회)
    Board getDetail(Long id);

    // 게시글 저장
    Long save(BoardSaveRequest request);

    // 게시글 수정
    void update(Long id, BoardSaveRequest request);

    // 게시글 및 첨부파일 물리 삭제
    void delete(Long id);
}