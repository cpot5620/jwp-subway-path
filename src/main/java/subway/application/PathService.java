package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.fare.DistanceFareStrategies;
import subway.domain.fare.Fare;
import subway.domain.line.Line;
import subway.domain.path.JgraphPathFinder;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.station.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final DistanceFareStrategies fareStrategies;

    public PathService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final DistanceFareStrategies fareStrategies
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.fareStrategies = fareStrategies;
    }

    public PathResponse findPath(final PathRequest request) {
        final List<Line> lines = lineRepository.findAllLine();
        final Station departure = stationRepository.findById(request.getDepartureStationId());
        final Station arrival = stationRepository.findById(request.getArriveStationId());

        final PathFinder pathFinder = new JgraphPathFinder(lines);
        final Path path = pathFinder.findPath(departure, arrival);

        final Fare totalFare = fareStrategies.getTotalFare(path.getPathDistance());

        return PathResponse.from(path, totalFare);
    }
}
