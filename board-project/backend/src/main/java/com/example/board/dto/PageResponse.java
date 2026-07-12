package com.example.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int page;        // 현재 페이지 (1부터 시작)
    private int size;        // 페이지당 개수
    private long totalElements;
    private int totalPages;

    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }
}
