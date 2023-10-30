package com.example.board.controller;

import com.example.board.dto.request.BoardRegisterRequest;
import com.example.board.dto.response.BoardResponse;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> registerBoard(@RequestBody BoardRegisterRequest request) {
        BoardResponse boardResponse = boardService.registerBoard(request);

        return ResponseEntity.ok(boardResponse);
    }

    @GetMapping
    public ResponseEntity<?> getBoardList() {
        List<BoardResponse> boardResponseList = boardService.getBoardList();

        return ResponseEntity.ok(boardResponseList);
    }

    @GetMapping("/{id:[0-9]*}")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        log.info("getBoard method / input id : " + id);
        BoardResponse board = boardService.getBoard(id);

        return ResponseEntity.ok(board);
    }
}
