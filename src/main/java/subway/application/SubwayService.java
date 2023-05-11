package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.SubwayDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.StationEnrollRequest;
import subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubwayService {

    private final SubwayDao subwayDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SubwayService(SubwayDao subwayDao, LineDao lineDao, StationDao stationDao) {
        this.subwayDao = subwayDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public void enrollStation(Long lineId, StationEnrollRequest request) {
        Line line = subwayDao.findById(lineId);
        Section section = new Section(
                stationDao.findById(request.getFromStation()),
                stationDao.findById(request.getToStation()),
                new Distance(request.getDistance())
        );
        line.addSection(section);
        subwayDao.save(line);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = subwayDao.findById(lineId);

        line.deleteStation(stationDao.findById(stationId));
        subwayDao.save(line);
    }

    public List<StationResponse> findRouteMap(Long lineId) {
        Line line = subwayDao.findById(lineId);
        return line.routeMap()
                .getRouteMap()
                .stream()
                .map(station -> new StationResponse(
                        station.getId(),
                        station.getName()
                )).collect(Collectors.toList());
    }

    public Map<String, List<StationResponse>> findAllRouteMap() {
        List<Line> allLines = lineDao.findAll().stream()
                .map(line -> subwayDao.findById(line.getId()))
                .collect(Collectors.toList());
        Map<String, List<StationResponse>> allRouteMap = new HashMap<>();
        for (Line line : allLines) {
            allRouteMap.put(line.getName(), findRouteMap(line.getId()));
        }
        return allRouteMap;
    }
}
