package subway.domain.farecalculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import subway.domain.farecalculator.policy.distance.BasicFareByDistancePolicy;
import subway.domain.farecalculator.policy.distance.FareByDistancePolicy;

class BasicFareByDistancePolicyTest {

    FareByDistancePolicy fareByDistancePolicy = new BasicFareByDistancePolicy();

    public static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(10,1250),
                Arguments.of(15,1350),
                Arguments.of(50,2050),
                Arguments.of(58,2150),
                Arguments.of(59,2250)
        );
    }

    @ParameterizedTest(name = "거리별 운임을 계산한다.")
    @MethodSource("provideDistanceAndFare")
    void calculateFare(int distance, int expected) {
        //when
        final int result = fareByDistancePolicy.calculateFare(distance);

        //then
        assertThat(result).isEqualTo(expected);
    }
}
