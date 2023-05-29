package subway.ui.controller.path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.path.usecase.FindShortestPathUseCase;
import subway.ui.dto.response.ShortestPathResponse;

@RestController
@RequestMapping("/path")
public class FindShortestPathController {

    private final FindShortestPathUseCase findShortestPathService;

    public FindShortestPathController(final FindShortestPathUseCase findShortestPathService) {
        this.findShortestPathService = findShortestPathService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> findShortestPath(
            @RequestParam final Long startStationId,
            @RequestParam final Long endStationId,
            @RequestParam(required = false, defaultValue = "0") final int passengerAge
    ) {
        final ShortestPathResponse shortestPath
                = findShortestPathService.findShortestPath(startStationId, endStationId, passengerAge);

        return ResponseEntity.ok(shortestPath);
    }
}
