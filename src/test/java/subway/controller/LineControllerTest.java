package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.controller.dto.LineRequest;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.SectionDeleteRequest;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.domain.line.Station;

@WebMvcTest(controllers = LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("노선을 생성한다.")
    void createLine() throws Exception {
        given(lineService.saveLine(any())).willReturn(createMockLine("1호선"));

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineRequest("1호선", 500)))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("노선 이름이 공백으로 요청되는 경우 400 BAD REQUEST가 반환된다.")
    void createLineFail() throws Exception {
        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineRequest("", 500)))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("노선 이름은 빈 값이 될 수 없습니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("노선 추가 요금이 음수로 요청되는 경우 400 BAD REQUEST가 반환된다.")
    void createLineFailWithWrongFare() throws Exception {
        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LineRequest("1호선", -500)))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("추가 금액은 0원 이상부터 가능합니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("노선 ID에 해당하는 정보를 가져온다.")
    void findLineById() throws Exception {
        Long id = 1L;
        given(lineService.findLineById(any())).willReturn(
                new Line(id, "1호선", new Fare(500), new Sections(new LinkedList<>())));

        mockMvc.perform(get("/lines/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("모든 노선의 모든 역 정보를 가져온다.")
    void findAllLines() throws Exception {
        Line line1 = createMockLine("1호선");
        Line line2 = createMockLine("2호선");
        given(lineService.findAllLines()).willReturn(List.of(line1, line2));

        mockMvc.perform(get("/lines").
                        contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("ID에 해당하는 노선을 삭제한다.")
    void deleteById() throws Exception {
        Long id = 1L;
        willDoNothing().given(lineService).deleteLineById(any());

        mockMvc.perform(delete("/lines/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("해당 노선에 역을 추가한다.")
    void createSection() throws Exception {
        Long id = 1L;
        willDoNothing().given(lineService).createSection(any(), any());

        mockMvc.perform(post("/lines/{id}/sections", id)
                        .content(objectMapper.writeValueAsString(new SectionCreateRequest("서면역", "강남역", 10)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @DisplayName("해당 노선에 역을 추가할 때, 역 이름이 공백이면 400 BAD REQUEST가 발생한다.")
    void createSectionFail() throws Exception {
        Long id = 1L;
        willDoNothing().given(lineService).createSection(any(), any());

        mockMvc.perform(post("/lines/{id}/sections", id)
                        .content(objectMapper.writeValueAsString(new SectionCreateRequest("", "", 10)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("역 이름은 빈 값이 될 수 없습니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("노선에 역을 삭제한다.")
    void deleteSection() throws Exception {
        Long id = 1L;
        willDoNothing().given(lineService).deleteSection(any(), any());

        mockMvc.perform(delete("/lines/{id}/sections", id)
                        .content(objectMapper.writeValueAsString(new SectionDeleteRequest("서울역")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    @DisplayName("해당 노선에 역을 삭제할 때, 역 이름이 공백이면 400 BAD REQUEST가 발생한다.")
    void deleteSectionFail() throws Exception {
        Long id = 1L;
        willDoNothing().given(lineService).createSection(any(), any());

        mockMvc.perform(delete("/lines/{id}/sections", id)
                        .content(objectMapper.writeValueAsString(new SectionDeleteRequest("")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("역 이름은 빈 값이 될 수 없습니다.")))
                .andDo(print());
    }

    private Line createMockLine(String name) {
        Station station1 = new Station(1L, "잠실역");
        Station station2 = new Station(2L, "강남역");
        Station station3 = new Station(3L, "선릉역");

        Section section1 = new Section(null, station1, station2, new Distance(5));
        Section section2 = new Section(null, station2, station3, new Distance(7));

        return new Line(null, name, new Fare(500), new Sections(new LinkedList<>(List.of(section1, section2))));
    }
}
