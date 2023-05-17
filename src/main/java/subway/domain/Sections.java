package subway.domain;

import subway.application.exception.SubwayInternalServerException;

import java.util.List;

public class Sections {

    private static final String INVALID_SECTIONS_SIZE_MESSAGE = "구간의 크기가 2를 초과합니다.";
    private static final String INVALID_ACCESS_SECTIONS_MESSAGE = "구간이 존재하지 않습니다.";
    private static final int MAX_SECTIONS_SIZE = 2;

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        if (sections.size() > MAX_SECTIONS_SIZE) {
            throw new SubwayInternalServerException(INVALID_SECTIONS_SIZE_MESSAGE);
        }
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Long getLeftStationId() {
        if (sections.size() == 1) {
            return sections.get(0).getLeftId();
        }
        if (sections.get(0).getLeft().equals(sections.get(1).getRight())) {
            return sections.get(1).getLeftId();
        }
        return sections.get(0).getLeftId();
    }

    public Long getRightStationId() {
        if (sections.size() == 1) {
            return sections.get(0).getRightId();
        }
        if (sections.get(0).getRight().equals(sections.get(1).getLeft())) {
            return sections.get(1).getRightId();
        }
        return sections.get(0).getRightId();
    }

    public boolean isPresent() {
        return !sections.isEmpty();
    }

    public Section getFirstSection() {
        if (isPresent()) {
            return sections.get(0);
        }
        throw new SubwayInternalServerException(INVALID_ACCESS_SECTIONS_MESSAGE);
    }
}
