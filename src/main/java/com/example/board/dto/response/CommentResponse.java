package com.example.board.dto.response;

import com.example.board.entity.Board;
import com.example.board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class CommentResponse {

    private String text;

    @Builder
    public CommentResponse(Comment comment) {
        this.text = comment.getText();
    }
}
