package subway.domain.section;

public enum Direction {

    UP {
        @Override
        public DirectionStrategy getDirectionStrategy() {
            return new UpDirectionStrategy();
        }
    }, DOWN {
        @Override
        public DirectionStrategy getDirectionStrategy() {
            return new DownDirectionStrategy();
        }
    };

    public abstract DirectionStrategy getDirectionStrategy();
}
