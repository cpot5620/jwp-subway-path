package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.helper.RestDocsHelper.constraint;
import static subway.helper.RestDocsHelper.prettyDocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.discount.AgeDiscountService;
import subway.application.path.PathService;
import subway.application.price.PriceService;
import subway.domain.path.Path;
import subway.domain.price.Price;
import subway.domain.station.Station;

@WebMvcTest(PathController.class)
@AutoConfigureRestDocs
class PathControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PathService pathService;

    @MockBean
    PriceService priceService;

    @MockBean
    AgeDiscountService ageDiscountService;

    @Test
    @DisplayName("/path로 GET 요청과 함께 path의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void findPath_success() throws Exception {
        // given
        String originStationName = "서울역";
        String destinationStationName = "용산역";
        String age = "20";
        given(pathService.findPath(anyString(), anyString()))
                .willReturn(new Path(
                        List.of(
                                new Station("서울역"),
                                new Station("잠실역"),
                                new Station("이수역"),
                                new Station("용산역")
                        ),
                        28,
                        Set.of(1L,2L,3L)
                ));
        given(ageDiscountService.discount(any(),any()))
                .willReturn(Price.from(1650));


        // expect
        mockMvc.perform(get("/path?originStation={originStationName}&destinationStation={destinationStationName}&age={age}",
                                originStationName, destinationStationName, age)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(prettyDocument("path/find",
                        requestParameters(
                                RequestDocumentation.
                                parameterWithName("originStation").description("출발역")
                                        .attributes(constraint("10글자 이내")),
                                parameterWithName("destinationStation").description("도착역")
                                        .attributes(constraint("10글자 이내")),
                                parameterWithName("age").description("나이")
                                        .attributes(constraint("0~150살 이내"))
                        ),
                        relaxedResponseFields(
                                fieldWithPath("result.stations").description("역 목록"),
                                fieldWithPath("result.totalDistance").description("총 거리"),
                                fieldWithPath("result.price").description("요금")
                        )
                ));
    }

    @ParameterizedTest(name = "/path로 GET 요청을 보낼 때, 나이가 {0}이면 HTTP 400 코드와 예외가 반환되어야 한다.")
    @MethodSource("illegalAge")
    void findPath_illegalAge(String condition, String age) throws Exception {
        // given
        String originStationName = "서울역";
        String destinationStationName = "용산역";

        // expect
        mockMvc.perform(
                        get("/path?originStation={originStationName}&destinationStation={destinationStationName}&age={age}",
                                originStationName, destinationStationName, age)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.age").exists());
    }

    static Stream<Arguments> illegalAge() {
        return Stream.of(
                Arguments.of("음수", "-1"),
                Arguments.of("null", null),
                Arguments.of("151살", "151"),
                Arguments.of("숫자가 아닌 문자열", "글렌")
        );
    }
}
