package subway.domain.Path;

import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class PathTestFixture {
    public static final Station 잠실나루 = Station.from("잠실나루");
    public static final Station 잠실 = Station.from("잠실");
    public static final Station 강변 = Station.from("강변");

    public static final Station 강동구청 = Station.from("강동구청");
    public static final Station 몽촌토성 = Station.from("몽촌토성");
    public static final Station 석촌 = Station.from("석촌");

    public static final Line _2호선 = Line.of("2호선", "초록색", 1000);
    public static final Line _8호선 = Line.of("8호선", "분홍색", 500);

    public static final Section 강변_잠실나루 = Section.of(_2호선, 강변, 잠실나루, 3);
    public static final Section 잠실나루_잠실 = Section.of(_2호선, 잠실나루, 잠실, 7);

    public static final Section 강동구청_몽촌토성 = Section.of(_8호선, 강동구청, 몽촌토성, 15);
    public static final Section 몽촌토성_잠실 = Section.of(_8호선, 몽촌토성, 잠실, 4);
    public static final Section 잠실_석촌 = Section.of(_8호선, 잠실, 석촌, 9);
}
