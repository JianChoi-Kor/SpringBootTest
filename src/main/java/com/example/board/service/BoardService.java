package com.example.board.service;

import com.example.board.dto.request.BoardRegisterRequest;
import com.example.board.dto.response.BoardResponse;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponse registerBoard(BoardRegisterRequest boardRegisterRequest) {
        Board savedBoard = boardRepository.save(boardRegisterRequest.toEntity());

        return BoardResponse.builder().board(savedBoard).build();
    }

    public List<BoardResponse> getBoardList() {
        List<Board> boardList = boardRepository.findAll();

        return boardList.stream().map(BoardResponse::new).collect(Collectors.toList());
    }

    public BoardResponse getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found Board"));

        return BoardResponse.builder().board(board).build();
    }
}
