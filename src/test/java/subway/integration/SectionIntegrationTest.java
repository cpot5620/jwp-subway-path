package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.StationRepository;
import subway.presentation.dto.request.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionIntegrationTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("truncate table LINE");
        jdbcTemplate.execute("truncate table STATION");
        jdbcTemplate.execute("truncate table SECTION");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("특정 구간을 생성할 수 있다.")
    @Test
    void createSection() {
        Long lineId = lineRepository.save(new Line("1호선"));
        SectionRequest sectionRequest = new SectionRequest("회기", "청량리", 10);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/subway/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("특정 구간을 삭제할 수 있다.")
    @Test
    void deleteSection() {
        Long lineId = lineRepository.save(new Line("1호선"));
        SectionRequest sectionRequest1 = new SectionRequest("회기", "청량리", 5);
        SectionRequest sectionRequest2 = new SectionRequest("청량리", "용산", 5);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/subway/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/subway/lines/" + lineId + "/sections")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        Long 청량리Id = stationRepository.findIdByName("청량리");
        RestAssured.given().log().all()
                .when().delete("/subway/lines/" + lineId + "/stations/" + 청량리Id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
