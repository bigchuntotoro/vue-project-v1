package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.dto.BoardSaveRequest;
import com.example.board.dto.BoardSearchCondition;
import com.example.board.dto.PageResponse;
import com.example.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    // application.properties에 설정된 파일 저장 경로 로드
    @Value("${file.dir:C:/upload/}")
    private String fileDir;

    @Override
    public PageResponse<Board> getList(BoardSearchCondition cond) {
        List<Board> content = boardMapper.findAll(cond);
        long total = boardMapper.count(cond);
        return new PageResponse<>(content, cond.getPage(), cond.getSize(), total);
    }

    @Override
    @Transactional
    public Board getDetail(Long id) {
        boardMapper.increaseViewCount(id);
        Board board = boardMapper.findById(id);
        if (board == null) {
            throw new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + id);
        }
        return board;
    }

    @Override
    @Transactional
    public Long save(BoardSaveRequest request) {
        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setWriter(request.getWriter());
        board.setContent(request.getContent());

        // 📎 첨부파일이 존재하는 경우 처리
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            saveUploadedFile(board, request.getFile());
        }

        boardMapper.insert(board);
        return board.getId();
    }

    @Override
    @Transactional
    public void update(Long id, BoardSaveRequest request) {
        Board board = boardMapper.findById(id);
        if (board == null) {
            throw new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + id);
        }
        board.setTitle(request.getTitle());
        board.setWriter(request.getWriter());
        board.setContent(request.getContent());

        // 📎 수정 시 새로운 첨부파일이 들어온 경우 처리
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            saveUploadedFile(board, request.getFile());
        }

        boardMapper.update(board);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 1. 삭제하기 전 DB에서 먼저 기존 게시글 및 파일 정보 조회
        Board board = boardMapper.findById(id);
        if (board == null) {
            throw new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + id);
        }

        // 2. 서버 로컬 디렉토리에 저장되어 있는 물리 파일 삭제 진행
        String storeFileName = board.getStoreFileName();
        if (storeFileName != null && !storeFileName.trim().isEmpty()) {
            try {
                File file = new File(fileDir + storeFileName);
                if (file.exists()) {
                    if (file.delete()) {
                        log.info("📌 [파일 삭제 완료] 물리 경로에서 파일이 성공적으로 제거되었습니다. 파일명: {}", storeFileName);
                    } else {
                        log.warn("⚠ [파일 삭제 실패] 파일은 존재하나 시스템 권한 문제 등으로 삭제하지 못했습니다. 파일명: {}", storeFileName);
                    }
                } else {
                    log.warn("⚠ [파일 제거 패스] 삭제 대상 파일이 물리 디렉토리에 존재하지 않습니다. 파일명: {}", storeFileName);
                }
            } catch (Exception e) {
                log.error("💥 [파일 삭제 에러] 물리 파일 삭제 진행 중 오류 발생: {}", e.getMessage());
            }
        }

        // 3. DB 데이터(게시글 레코드) 삭제 진행
        int deleted = boardMapper.delete(id);
        if (deleted == 0) {
            throw new NoSuchElementException("게시글을 삭제할 수 없습니다. id=" + id);
        }
        log.info("📌 [게시글 DB 삭제 완료] ID: {}", id);
    }

    // 💡 [공용 파일 저장 메서드]
    private void saveUploadedFile(Board board, MultipartFile file) {
        try {
            File directory = new File(fileDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFileName = file.getOriginalFilename();
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            String storeFileName = UUID.randomUUID().toString() + ext;
            String filePath = fileDir + storeFileName;

            file.transferTo(new File(filePath));

            board.setOriginFileName(originalFileName);
            board.setStoreFileName(storeFileName);
            board.setFilePath(filePath);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 시스템 오류가 발생했습니다.", e);
        }
    }
}