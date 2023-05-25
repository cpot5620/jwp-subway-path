package subway.business.domain.subwaymap;

import java.util.List;
import subway.business.domain.line.Station;
import subway.business.domain.line.Stations;

public interface SubwayMap {

    List<Stations> calculateShortestPath(Station sourceStation, Station targetStation);

    int calculateDistanceOfPath(Station sourceStation, Station targetStation);
}
