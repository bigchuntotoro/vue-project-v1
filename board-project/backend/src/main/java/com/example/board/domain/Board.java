package com.example.board.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private Integer viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
