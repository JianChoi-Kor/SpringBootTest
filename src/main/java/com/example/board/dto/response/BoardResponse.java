package com.example.board.dto.response;

import com.example.board.entity.Board;
import com.example.board.entity.Comment;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private List<Comment> comments;

    @Builder
    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.comments = board.getComments();
    }
}
