package subway.controller.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.request.StationCreateRequest;
import subway.dto.response.LinesResponse;
import subway.dto.request.SectionCreateRequest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

import static io.restassured.RestAssured.given;
import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class LineControllerIntegrationTest {

	@Autowired
	private LineService lineService;

	@Autowired
	private StationService stationService;

	@Autowired
	private SectionService sectionService;

	@LocalServerPort
	private int port;

	String lineName;
	LineCreateRequest lineCreateRequest;
	@BeforeEach
	public void setUp() {
		RestAssured.port = this.port;
		lineName = "2호선";
		lineCreateRequest = new LineCreateRequest(lineName);
	}

	@Test
	@DisplayName("노선 생성 테스트")
	void createLine() {
		// when
		final ExtractableResponse<Response> response = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(lineCreateRequest)
			.when().post("/lines")
			.then().extract();
		final String decodedHeader = decode(response.header("Location"), UTF_8);

		// then
		assertAll(
			() -> assertThat(decodedHeader).isEqualTo("/lines/" + lineName),
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
		);
	}

	@Test
	@DisplayName("모든 노선 조회 테스트")
	void findAll() {
		// given
		lineService.saveLine(lineCreateRequest);

		// when
		final ExtractableResponse<Response> response = given()
			.when()
			.get("/lines")
			.then().log().all()
			.extract();

		// then
		assertAll(
			()-> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			()-> Assertions.assertThat(response.body().jsonPath().get("lines[0].name").toString()).isEqualTo("2호선")
		);
	}

	@Test
	@DisplayName("노선 조회 테스트")
	void findByName() {
		// given
		lineService.saveLine(lineCreateRequest);

		StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
		StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
		stationService.saveStation(stationCreateRequest);
		stationService.saveStation(stationCreateRequest2);

		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3L);
		sectionService.insertSection(sectionCreateRequest);

		String lineName = lineCreateRequest.getName();

		// when & then
		Response response = given()
			.when().get("/lines/" + lineName);

		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("stations[0].id", equalTo(1))
			.body("stations[0].name", equalTo("잠실역"))
			.body("stations[1].id", equalTo(2))
			.body("stations[1].name", equalTo("잠실새내역"));
	}

	@Test
	@DisplayName("노선 갱신 테스트")
	void updateLine() {
		// given
		lineService.saveLine(lineCreateRequest);

		LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("신분당선");

		// when & then
		Response response = given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(lineUpdateRequest)
			.when().patch("/lines/2호선");

		response.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	@DisplayName("노선 삭제 테스트")
	void deleteLine() {
		// given
		lineService.saveLine(lineCreateRequest);

		// when & then
		Response response = given()
			.when().delete("/lines/2호선");

		response.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		LinesResponse lineEntities = lineService.findAll();
		assertThat(lineEntities.getLines().size()).isEqualTo(0);
	}
}
