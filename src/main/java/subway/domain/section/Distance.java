package subway.domain.section;

import java.util.Objects;

public class Distance {

    private final int distance;

    public Distance(final Integer distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final Integer distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isLowerOrEqualThan(final Distance distance) {
        return this.distance <= distance.distance;
    }

    public boolean isGreaterOrEqualThan(final Distance distance) {
        return this.distance >= distance.distance;
    }

    public Distance plus(final Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public Distance minus(final Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Distance otherDistance = (Distance) other;
        return Objects.equals(distance, otherDistance.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
