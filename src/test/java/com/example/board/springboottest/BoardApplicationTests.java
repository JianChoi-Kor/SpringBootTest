package com.example.board.springboottest;

import com.example.board.dto.request.BoardRegisterRequest;
import com.example.board.dto.response.BoardResponse;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import com.example.board.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//롤백을 위한 @Transactional 어노테이션
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext ctx;
	@Autowired
	private TestRestTemplate testRestTemplate;
	@LocalServerPort
	private int port;

	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardRepository boardRepository;

	@BeforeEach()
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
//				.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
				.alwaysDo(print())
				.build();
	}

	@DisplayName("게시글 등록 테스트")
	@Test
	void registerBoardTest() {
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);

		//register
		BoardResponse boardResponse = boardService.registerBoard(boardRegisterRequest);

		//Then
		Assertions.assertThat(requestTitle).isEqualTo(boardResponse.getTitle());
		Assertions.assertThat(requestContent).isEqualTo(boardResponse.getContent());
	}

	@DisplayName("게시글 조회 테스트")
	@Test
	void getBoardTest() {
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);
		BoardResponse savedBoardResponse = boardService.registerBoard(boardRegisterRequest);

		//getBoard
		BoardResponse getBoardResponse = boardService.getBoard(savedBoardResponse.getId());

		//Then
		Assertions.assertThat(savedBoardResponse.getTitle()).isEqualTo(getBoardResponse.getTitle());
		Assertions.assertThat(savedBoardResponse.getContent()).isEqualTo(getBoardResponse.getContent());
	}

	@DisplayName("게시글 전체 조회 테스트")
	@Test
	void getBoardListTest() {
		String title = "제목";
		String content = "내용";
		List<Board> boardList = new ArrayList<>();
		int count = 5;
		for (int i=0; i<count; i++) {
			boardList.add(Board.builder().title(title + i).content(content + i).build());
		}
		boardRepository.saveAll(boardList);

		//getBoardList
		List<BoardResponse> boardResponseList = boardService.getBoardList();

		//Then
		Assertions.assertThat(boardResponseList.size()).isEqualTo(count);
	}

	@DisplayName("게시글 조회 테스트 with MockMvc")
	@Test
	void getBoardTestWithMockMvc() throws Exception {
		//Given
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);
		BoardResponse savedBoardResponse = boardService.registerBoard(boardRegisterRequest);

		//When, Then
		mockMvc.perform(get("/board/" + savedBoardResponse.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.title", is(requestTitle)))
				.andDo(print());
	}

	@DisplayName("게시글 등록 테스트 with MockMvc")
	@Test
	void registerBoardTestWithMockMvc() throws Exception {
		//Given
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);

		String content = objectMapper.writeValueAsString(boardRegisterRequest);

		mockMvc.perform(post("/board")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	//rollback 안됨
	@DisplayName("게시글 조회 테스트 with TestRestTemplate")
	@Test
	void getBoardTestWithTestRestTemplate() {
		//Given
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);

		String registerUrl = "http://localhost:" + this.port + "/board";

		//When
		ResponseEntity<BoardResponse> responseEntity
				= testRestTemplate.postForEntity(registerUrl, boardRegisterRequest, BoardResponse.class);

		String findUrl = "http://localhost:" + this.port + "/board/" + responseEntity.getBody().getId();

		ResponseEntity<BoardResponse> response =
				testRestTemplate.getForEntity(findUrl, BoardResponse.class);
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(response.getBody()).isNotNull();
	}

	//rollback 안됨
	@DisplayName("게시글 등록 테스트 with TestRestTemplate")
	@Test
	void registerBoardTestWithTestRestTemplate() {
		//Given
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);

		String url = "http://localhost:" + this.port + "/board";

		//When
		ResponseEntity<BoardResponse> responseEntity
				= testRestTemplate.postForEntity(url, boardRegisterRequest, BoardResponse.class);

		//Then
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getBody().getTitle()).isEqualTo(boardRegisterRequest.getTitle());
	}

	//다음 경우 test failed... 왜?
	@DisplayName("게시글 조회 테스트 안되는 경우")
	@Test
	void getBoardTestNotWorking() {
		//Given
		String requestTitle = "제목";
		String requestContent = "내용";
		BoardRegisterRequest boardRegisterRequest = new BoardRegisterRequest(requestTitle, requestContent);
		BoardResponse boardResponse = boardService.registerBoard(boardRegisterRequest);

		String url = "http://localhost:" + this.port + "/board/" + boardResponse.getId();

		ResponseEntity<BoardResponse> response =
				testRestTemplate.getForEntity(url, BoardResponse.class);
		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(response.getBody()).isNotNull();
	}
}
