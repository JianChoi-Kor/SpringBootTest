package com.example.board.dto.request;

import com.example.board.entity.Board;
import com.example.board.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRegisterRequest {

    private Long boardId;
    private String text;

    public Comment toEntity() {
        return Comment.builder()
                .board(Board.of(boardId))
                .text(text)
                .build();
    }
}
