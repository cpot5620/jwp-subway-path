package subway.ui.dto;

public class SectionDeleteRequest {

    private Long lineId;
    private String stationName;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(Long lineId, String stationName) {
        this.lineId = lineId;
        this.stationName = stationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getStationName() {
        return stationName;
    }
}
