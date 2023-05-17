package subway.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.dto.CreateSectionDto;

public class CreateSectionResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<ReadStationResponse> stationResponses;

    public CreateSectionResponse(
            final Long id,
            final String name,
            final String color,
            final List<ReadStationResponse> stationResponses
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static CreateSectionResponse from(final CreateSectionDto dto) {
        final List<ReadStationResponse> stationResponses = dto.getStationDtos().stream()
                .map(ReadStationResponse::from)
                .collect(Collectors.toList());

        return new CreateSectionResponse(dto.getId(), dto.getName(), dto.getColor(), stationResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<ReadStationResponse> getStationResponses() {
        return stationResponses;
    }
}
