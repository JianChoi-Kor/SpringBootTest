package com.example.board.service;

import com.example.board.dto.request.CommentRegisterRequest;
import com.example.board.dto.response.CommentResponse;
import com.example.board.entity.Comment;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentResponse registerComment(CommentRegisterRequest commentRegisterRequest) {
        boardRepository.findById(commentRegisterRequest.getBoardId())
                .orElseThrow(() -> new RuntimeException("Not found Board"));

        Comment savedComment = commentRepository.save(commentRegisterRequest.toEntity());

        return CommentResponse.builder().comment(savedComment).build();
    }
}
