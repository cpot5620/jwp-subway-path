package subway.dto;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    private final Long line;
    private final String fromStation;
    private final String toStation;
    private final int distance;

    public SectionResponse(final Long line, final String fromStation, final String toStation, final int distance) {
        this.line = line;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.distance = distance;
    }

    public static SectionResponse from(final Section section) {
        return new SectionResponse(section.getLindId(),
                section.getUpper().getName(),
                section.getLower().getName(),
                section.getDistance().getValue());
    }

    public static List<SectionResponse> toList(final List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::from)
                .collect(Collectors.toList());
    }

    public Long getLine() {
        return line;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public int getDistance() {
        return distance;
    }
}
