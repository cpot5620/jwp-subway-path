package subway.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @DisplayName("구간이 정상적으로 생성된다.")
    @Test
    void createSection() {
        // given
        Station leftStation = new Station("잠실역");
        Station rightStation = new Station("강남역");
        Distance distance = new Distance(5);

        // when
        Section section = new Section(leftStation, rightStation, distance);

        // then
        assertSoftly(softly -> {
            softly.assertThat(section.getLeft()).isEqualTo(leftStation);
            softly.assertThat(section.getRight()).isEqualTo(rightStation);
            softly.assertThat(section.getDistance()).isEqualTo(distance.getValue());
        });
    }
}
