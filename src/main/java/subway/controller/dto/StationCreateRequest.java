package subway.controller.dto;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String name;

    private StationCreateRequest() {

    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
