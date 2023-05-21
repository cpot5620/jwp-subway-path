package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.fare.AgeGroup;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FareInformation;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.dto.request.ShortestPathRequest;
import subway.dto.response.ShortestPathResponse;
import subway.exception.NotFoundStationException;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public PathService(
            final StationRepository stationRepository,
            final LineRepository lineRepository,
            final PathFinder pathFinder,
            final FareCalculator fareCalculator
    ) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest request) {
        final Station startStation = findStationByName(request.getStartStation());
        final Station endStation = findStationByName(request.getEndStation());
        final Lines lines = lineRepository.findAll();
        final Path path = pathFinder.findShortestPath(startStation, endStation, lines);
        final int fee = fareCalculator.calculate(getFeeInformation(path, lines, request.getAge()));

        return ShortestPathResponse.of(path, fee);
    }

    private Station findStationByName(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundStationException(name));
    }

    private FareInformation getFeeInformation(final Path path, final Lines lines, final int age) {
        Lines findLines = lines.findLinesByContainSection(path.getSections());
        return new FareInformation(path.getTotalDistance(), findLines, AgeGroup.from(age));
    }
}

