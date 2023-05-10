package subway.domain;

public class Section {

    private final Station up;
    private final Station down;
    private final int distance;

    public Section(final Station up, final Station down, final int distance) {
        validateDistance(distance);
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역간 거리는 양수여야 합니다.");
        }
    }

    public Section minus(final Section addedSection) {
        final int dist = this.distance - addedSection.distance;
        if (dist <= 0) {
            throw new IllegalArgumentException("현재 구간이 더 작아 차이를 구할 수 없습니다.");
        }
        if (this.up.equals(addedSection.up)) {
            return new Section(addedSection.down, this.down, dist);
        }
        if (this.down.equals(addedSection.down)) {
            return new Section(this.up, addedSection.up, dist);
        }
        throw new IllegalArgumentException("두 구간이 연관관계가 없어 뺄 수 없습니다.");
    }

    public boolean isDownThan(final Section section) {
        return this.up.equals(section.down);
    }

    public boolean hasSameUpOrDownStation(final Section section) {
        return  this.up.equals(section.up) || this.down.equals(section.down);
    }

    public Station getUp() {
        return up;
    }

    public Station getDown() {
        return down;
    }

    public int getDistance() {
        return distance;
    }
}
