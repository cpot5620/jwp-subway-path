package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.GlobalException;

class SectionTest {
    private static final Distance DISTANCE = new Distance(10);

    @DisplayName("도착 역과 시작 역은 같을 수 없다.")
    @Test
    void createSectionSuccessTest() {
        Station startStation = new Station("회기역");
        Station endStation = new Station("청량리");

        assertDoesNotThrow(() -> new Section(startStation, endStation, DISTANCE));
    }

    @DisplayName("도착 역과 시작 역은 같을 수 없다.")
    @Test
    void createSectionFailTestBySameStation() {
        Station startStation = new Station("회기역");
        Station endStation = new Station("회기역");

        assertThatThrownBy(() -> new Section(startStation, endStation, DISTANCE))
                .isInstanceOf(GlobalException.class)
                .hasMessage("시작 역과 도착 역은 같을 수 없습니다.");
    }
}
