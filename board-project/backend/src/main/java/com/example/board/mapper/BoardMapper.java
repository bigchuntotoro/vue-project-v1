package com.example.board.mapper;

import com.example.board.domain.Board;
import com.example.board.dto.BoardSearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시글 목록 조회 (검색 + 페이징)
    List<Board> findAll(@Param("cond") BoardSearchCondition cond);

    // 조건에 맞는 전체 게시글 수
    long count(@Param("cond") BoardSearchCondition cond);

    // 게시글 단건 조회
    Board findById(@Param("id") Long id);

    // 게시글 등록
    void insert(Board board);

    // 게시글 수정
    int update(Board board);

    // 게시글 삭제
    int delete(@Param("id") Long id);

    // 조회수 증가
    void increaseViewCount(@Param("id") Long id);
}
