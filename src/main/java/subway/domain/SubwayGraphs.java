package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.entity.EdgeEntity;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class SubwayGraphs {
    private final List<SubwayGraph> subwayGraphs;
    private final WeightedMultigraph subwayMap;


    public SubwayGraphs(List<SubwayGraph> subwayGraphs) {
        this.subwayGraphs = new ArrayList<>(subwayGraphs);
        this.subwayMap = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public SubwayGraphs() {
        this(Collections.emptyList());
    }

    public void addLine(Line line) {
        SubwayGraph subwayGraph = new SubwayGraph(line);
        subwayGraphs.add(subwayGraph);
    }

    public EdgeEntity findEdge(Line line, Station station) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.findEdgeEntity(station);
    }

    public List<Station> findAllStationsInOrder(Line line) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.findAllStationsInOrder();
    }

    public List<Station> addStation(final Line line, final Station upLineStation, final Station downLineStation, final int distance) {
        final SubwayGraph subwayGraph = findSubwayGraph(line);
        return subwayGraph.addStation(upLineStation, downLineStation, distance);
    }

    public SubwayGraph findSubwayGraph(final Line line) {
        final SubwayGraph lineGraph = subwayGraphs.stream()
                .filter(s -> s.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException());
        return lineGraph;
    }

    public boolean isStationExistInAnyLine(Station station) {
        return subwayGraphs.stream()
                .anyMatch(subwayGraph -> subwayGraph.isStationExist(station));
    }

    public List<Station> remove(Line line) {
        SubwayGraph subwayGraph = findSubwayGraph(line);
        List<Station> removedStations = subwayGraph.clear();
        subwayGraphs.remove(subwayGraph);
        return removedStations;
    }

    public void deleteStation(Line line, Station station) {
        SubwayGraph subwayGraph = findSubwayGraph(line);
        subwayGraph.delete(station);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getSubwayMap() {
        Graphs.removeVertexAndPreserveConnectivity(subwayMap, subwayMap.vertexSet().iterator());
        for (SubwayGraph subwayGraph : this.subwayGraphs) {
            Graphs.addGraph(subwayMap, subwayGraph.getMultiGraph());
        }
        return subwayMap;
    }

    public ShortestPath getShortestPath(Station source, Station destination) {
        validateStationExist(source);
        validateStationExist(destination);

        DijkstraShortestPath calculator = new DijkstraShortestPath(getSubwayMap());
        GraphPath<Station, DefaultWeightedEdge> path = calculator.getPath(source, destination);
        return new ShortestPath(path.getVertexList(), (int) path.getWeight());
    }

    private void validateStationExist(Station source) {
        if (!isStationExistInAnyLine(source)) {
            throw new StationNotFoundException();
        }
    }
}