# jwp-subway-path

## 요구사항

### API 요구사항

- [x] 노선에 역 등록 API 신규 구현
- [x] 노선에 역 제거 API 신규 구현
- [x] 노선 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선
- [x] 노선 목록 조회 API 수정
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선

### 도매인 요구사항

#### 노선 역 등록

##### 노선 등록 정보

- [x] 구간
    - [x] 좌측 역
    - [x] 우측 역
    - [x] 거리
- [x] 구간 연결 정보 (노선 별 역 연결 정보)
- [x] 노선도 (노선 별 상헹-하행 역 정보)
    - [x] 노선
    - [x] 역 연결 정보
    - [x] 시작 역

##### 노선 등록 조건

- [x] 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야한다.
- [x] 하나의 역은 여러 노선에 등록이 될 수 있다.
- [x] 노선은 갈래길을 가질 수 없다.
- [x] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야한다.
- [x] 노선 가운데 역이 등록 될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야한다.

#### 노선 역 제거

- [x] 노선에서 역을 제거할 경우 정상 동작을 위해 역을 재배치 되어야한다.
- [x] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야한다.
- [x] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야한다.

## API 명세

### 역 등록

#### Request

- 역 등록은 기준역의 다음에 새로운 역을 추가합니다.
- 상행, 하행 방향을 지정해주어야 합니다.

```http request
POST /sections HTTP/1.1
{
    "lineId": 1,
    "section":  {
        "baseStationId": 1,
        "nextStationId": 2,
        "distance": 2,
    },
    "direction": up
}
```

#### Response

```http request
HTTP/1.1 201
```

### 역 제거

#### Request

```http request
DELETE /sections/{lineId}/{stationId} HTTP/1.1
```

#### Response

```http request
HTTP/1.1 204
```
