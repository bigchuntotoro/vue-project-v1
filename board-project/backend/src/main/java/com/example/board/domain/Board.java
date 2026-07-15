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
    // 🎯 [필수 추가] DB 컬럼들과 매핑될 필드들입니다.
    private int viewCount;              // view_count 컬럼과 매핑
    private LocalDateTime createdDate;  // created_date 컬럼과 매핑
    private LocalDateTime updatedDate;  // updated_date 컬럼과 매핑

    // 📎 [추가] 파일 정보 필드들 (Lombok @Data가 자동으로 Getter/Setter를 생성해 줍니다!)
    private String originFileName;
    private String storeFileName;
    private String filePath;
}