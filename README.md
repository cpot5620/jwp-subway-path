# jwp-subway-path

## API 기능 요구사항

- [x] 역 생성 API 구현
- [x] 노선에 역 등록 API 구현
- [x] 노선의 역 제거 API 구현
- [x] 새 노선 등록 API 구현
- [x] 노선 조회 API 구현
- [x] 노선 목록 조회 API 구현
- [x] 경로 조회 API 구현


[API 문서](apidocs/APIdocs.md)

## 비즈니스 요구사항

### 역 등록
- [x] 역을 추가할 수 있다.
  - [x] 같은 이름의 역을 생성할 수 없다.

### 노선 등록
- [x] 노선을 등록할 수 있다.
  - 이미 추가된 두 개의 역을 지정해야한다.
  - [x] 같은 이름의 노선을 등록할 수 없다.

### 노선에 역 등록
- [x] 노선에 등록되는 역의 위치를 지정할 수 있다.
  - 필요한 정보
    - 노선 아이디  
    - 인접한 역의 아이디
    - 역 등록 방향
    - 인접 역과의 거리
  - [x] 두 역 중간에 새로운 역을 등록시 역간의 거리 정보를 고려한다.
    - 거릭가 7인 A - B 에 역C를 등록시 A와의 거리는 7미만 양의정수여야 한다.  
- [x] 하나의 역은 여러 노선에 등록이 될 수 있다.
- [x] 노선은 갈래길을 가질 수 없다.

### 노선의 역 제거
- [x] 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 한다.
  - A-(2)-B-(3)-C 상황에서 B를 제거시 A-(5)-C 상태가 되어야 한다.
- [x] 역이 2개인 노선에서 역을 제거하면 노선이 사라진다.

### 경로 조회
- [x] 시작역과 도착역으로 경로를 조회한다.
  - [x] 역간거리를 이용하여 최단경로를 구한다.
  - [x] 각 경로에 해당하는 노선을 구한다.
- [x] 경로를 통해 이용 요금을 계산한다.
  - 기본운임(10㎞ 이내): 기본운임 1,250원
  - 이용 거리 초과 시 추가운임 부과
  - 10km~50km: 5km 까지 마다 100원 추가
  - 50km 초과: 8km 까지 마다 100원 추가
