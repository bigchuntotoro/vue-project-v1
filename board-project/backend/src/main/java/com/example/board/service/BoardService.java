package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.dto.BoardSaveRequest;
import com.example.board.dto.BoardSearchCondition;
import com.example.board.dto.PageResponse;
import com.example.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardMapper boardMapper;

    public PageResponse<Board> getList(BoardSearchCondition cond) {
        List<Board> content = boardMapper.findAll(cond);
        long total = boardMapper.count(cond);
        return new PageResponse<>(content, cond.getPage(), cond.getSize(), total);
    }

    @Transactional
    public Board getDetail(Long id) {
        boardMapper.increaseViewCount(id);
        Board board = boardMapper.findById(id);
        if (board == null) {
            throw new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + id);
        }
        return board;
    }

    @Transactional
    public Long save(BoardSaveRequest request) {
        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setWriter(request.getWriter());
        board.setContent(request.getContent());
        boardMapper.insert(board);
        return board.getId();
    }

    @Transactional
    public void update(Long id, BoardSaveRequest request) {
        Board board = boardMapper.findById(id);
        if (board == null) {
            throw new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + id);
        }
        board.setTitle(request.getTitle());
        board.setWriter(request.getWriter());
        board.setContent(request.getContent());
        boardMapper.update(board);
    }

    @Transactional
    public void delete(Long id) {
        int deleted = boardMapper.delete(id);
        if (deleted == 0) {
            throw new NoSuchElementException("게시글을 찾을 수 없습니다. id=" + id);
        }
    }
}
