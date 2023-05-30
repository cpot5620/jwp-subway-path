package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineColor;
import subway.domain.LineName;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long saveLine(final LineRequest request) {
        Line line = new Line(
                new LineName(request.getName()),
                new LineColor(request.getColor()),
                Sections.create());
        Line savedLine = lineRepository.save(line);
        return savedLine.getId();
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long lineId) {
        Line line = lineRepository.findById(lineId);
        return LineResponse.from(line);
    }

    @Transactional
    public void editLine(final Long lineId, final LineRequest request) {
        Line line = lineRepository.findById(lineId);
        Line updateLine = new Line(
                lineId,
                new LineName(request.getName()),
                new LineColor(request.getColor()),
                new Sections(line.getSections())
        );
        lineRepository.update(updateLine);
    }

    @Transactional
    public void deleteLine(final Long lineId) {
        Line line = lineRepository.findById(lineId);
        sectionRepository.deleteAll(line.getSections(), line);
        lineRepository.delete(line);
    }
}
