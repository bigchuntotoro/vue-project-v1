package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile; // 💡 임포트 추가

@Data
public class BoardSaveRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자를 넘을 수 없습니다.")
    private String title;

    @NotBlank(message = "작성자를 입력해주세요.")
    @Size(max = 50, message = "작성자명은 50자를 넘을 수 없습니다.")
    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    // 📎 [추가] 프론트엔드 FormData의 "file" 키값과 자동으로 매핑되는 멀티파트 파일 필드
    private MultipartFile file;
}