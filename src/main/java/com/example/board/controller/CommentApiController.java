package com.example.board.controller;

import com.example.board.dto.request.CommentRegisterRequest;
import com.example.board.dto.response.CommentResponse;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> registerComment(@RequestBody CommentRegisterRequest request) {
        CommentResponse commentResponse = commentService.registerComment(request);

        return ResponseEntity.ok(commentResponse);
    }
}
