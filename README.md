# URL 단축 서비스 (Short URL Service)

## 프로젝트 개요
이 서비스는 긴 URL을 짧은 URL로 변환하여 관리하고, 단축 URL의 접근 통계를 제공하는 Spring Boot 기반의 애플리케이션입니다.

## 시스템 요구사항
- Java 11
- Spring Boot 2.5.1
- MyBatis 2.2.0
- H2 Database

## 설치 및 실행 방법

### 프로젝트 클론
```
git clone git@github.com:yumin00/lg-shorturl.git
cd shorturl
```

### 애플리케이션 실행
```
./gradlew bootRun
```

## 프로젝트 구조
```
com.laundrygo.shorturl
├── controller # REST API 컨트롤러
├── service # 비즈니스 로직 구현
├── repository # 데이터 액세스 계층
├── domain # 도메인 모델 (엔티티)
├── dto # 데이터 전송 객체
│ ├── request # 요청 DTO
│ └── response # 응답 DTO
└── error # 예외 처리
```

## API 명세
### 1. URL 단축 API
- 엔드포인트: POST /short-urls
- 기능: 원본 URL을 8자리 랜덤 문자열로 구성된 단축 URL로 변환합니다.
- 요청 본문:
```
  {
    "url": "some-long-url"
  }
```
- 응답:
```
  {
    "shortUrl": "abcd1234"
  }
```
- 에러 응답:
    - 유효하지 않은 URL 입력 시 400 Bad Request

### 2. URL 리다이렉트 API
- 엔드포인트: GET /short-urls/{shortUrl}/redirect
- 기능: 단축 URL을 통해 원본 URL로 리다이렉트하며, 접근 로그를 기록합니다.
- 응답: 301 Moved Permanently (원본 URL로 리다이렉트)
- 에러 응답:
    - 존재하지 않는 단축 URL 입력 시 404 Not Found

### 3. 접근 통계 조회 API
- 엔드포인트: GET /short-urls/{shortUrl}/access-stats
- 기능: 특정 단축 URL의 최근 24시간 동안의 시간별 접근 통계를 제공합니다.
- 응답:
```
  {
    "originalUrl": "https://example.com/some-long-url",
    "hourlyStats": [
      {
        "datetime": "2023-05-20 14:00:00",
        "accessCount": 5
      },
      {
        "datetime": "2023-05-20 15:00:00",
        "accessCount": 3
      },
      ...
    ]
  }
```
- 에러 응답:
존재하지 않는 단축 URL 입력 시 404 Not Found