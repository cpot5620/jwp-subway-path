package subway.service;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.persistence.repository.line.JdbcLineRepository;
import subway.persistence.repository.section.JdbcSectionRepository;
import subway.persistence.repository.station.JdbcStationRepository;
import subway.service.section.SectionService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class SectionServiceTest {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private JdbcSectionRepository sectionRepository;

    @Autowired
    private JdbcLineRepository lineRepository;

    @Autowired
    private JdbcStationRepository stationRepository;

    @Test
    void 역_구간_정보를_추가한다() {
        lineRepository.save(new Line(2L, "2호선", "초록색"));
        stationRepository.save(new Station("잠실역"));
        stationRepository.save(new Station("아현역"));
        stationRepository.save(new Station("신촌역"));

        sectionService.addSection(new SectionCreateRequest(2L, "잠실역", "아현역", 5L));
        sectionService.addSection(new SectionCreateRequest(2L, "잠실역", "신촌역", 3L));

        final List<Section> sections = sectionRepository.findByLineNumber(2L).getSections();
        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sections.size()).isEqualTo(2);

        softAssertions.assertThat(sections.get(0).getUpStation()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Station("잠실역"));
        softAssertions.assertThat(sections.get(0).getDownStation()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Station("신촌역"));
        softAssertions.assertThat(sections.get(0).getDistance()).isEqualTo(3L);

        softAssertions.assertThat(sections.get(1).getUpStation()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Station("신촌역"));
        softAssertions.assertThat(sections.get(1).getDownStation()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Station("아현역"));
        softAssertions.assertThat(sections.get(1).getDistance()).isEqualTo(2L);
        softAssertions.assertAll();
    }

    @Test
    void 역_구간_정보를_삭제한다() {
        lineRepository.save(new Line(2L, "2호선", "초록색"));
        stationRepository.save(new Station("잠실역"));
        stationRepository.save(new Station("아현역"));
        stationRepository.save(new Station("신촌역"));
        sectionService.addSection(new SectionCreateRequest(2L, "잠실역", "아현역", 5L));
        sectionService.addSection(new SectionCreateRequest(2L, "잠실역", "신촌역", 3L));

        sectionService.removeSection(new SectionDeleteRequest(2L, "신촌역"));

        final List<Section> sections = sectionRepository.findByLineNumber(2L).getSections();
        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sections.size()).isEqualTo(1);

        softAssertions.assertThat(sections.get(0).getUpStation()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Station("잠실역"));
        softAssertions.assertThat(sections.get(0).getDownStation()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Station("아현역"));
        softAssertions.assertThat(sections.get(0).getDistance()).isEqualTo(5L);
        softAssertions.assertAll();
    }
}
