package subway.domain.path.strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;
import subway.domain.path.PathInfo;
import subway.domain.subway.Section;

public class PathFindByDistanceStrategy implements PathFindStrategy {
    @Override
    public PathInfo findPathInfo(Long departureId, Long destinationId, List<Section> allSections) {
        Set<Long> lineIds = new HashSet<>();
        Graph<Long, DefaultWeightedEdge> graph = makeGraph(allSections, lineIds);
        GraphPath<Long, DefaultWeightedEdge> path = getPath(departureId, destinationId, graph);

        return new PathInfo(path.getVertexList(), lineIds, (int)path.getWeight());
    }

    private Graph<Long, DefaultWeightedEdge> makeGraph(List<Section> allSections, Set<Long> lineIds) {
        Graph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        try {
            setGraph(allSections, lineIds, graph);
        } catch (IllegalArgumentException e) {
            throw new DomainException(ExceptionType.PATH_HAS_LOOP);
        }
        return graph;
    }

    private void setGraph(List<Section> allSections, Set<Long> lineIds, Graph<Long, DefaultWeightedEdge> graph) {
        for (Section section : allSections) {
            Long sourceStationId = section.getSourceStationId();
            Long targetStationId = section.getTargetStationId();
            lineIds.add(section.getLineId());
            graph.addVertex(sourceStationId);
            graph.addVertex(targetStationId);
            graph.setEdgeWeight(graph.addEdge(sourceStationId, targetStationId), section.getDistance());
            graph.setEdgeWeight(graph.addEdge(targetStationId, sourceStationId), section.getDistance());
        }
    }

    private GraphPath<Long, DefaultWeightedEdge> getPath(Long departureId, Long destinationId,
        Graph<Long, DefaultWeightedEdge> graph) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(departureId, destinationId);

        if (path == null) {
            throw new DomainException(ExceptionType.NO_PATH);
        }

        return path;
    }
}
