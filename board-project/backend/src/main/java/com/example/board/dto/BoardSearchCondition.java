package com.example.board.dto;

import lombok.Data;

@Data
public class BoardSearchCondition {
    private int page = 1;          // 현재 페이지 (1부터 시작)
    private int size = 10;         // 페이지당 개수
    private String searchType;     // title, writer, content
    private String keyword;        // 검색어

    public int getOffset() {
        return (page - 1) * size;
    }
}
