# Boom! Backend Server

**'요즘 나의 붐은?' 취향 아카이브 서비스 백엔드**

사용자의 취향을 카드 형태로 기록하고, PAEF(Productivity, Activity, Emotional, Focus) 점수 기반으로 취향을 분석하여 유사한 취향을 가진 사용자와 카드를 추천하는 서비스입니다.

---

## 🛠 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Security** - JWT 기반 인증
- **Spring Data JPA** - 데이터베이스 ORM
- **MySQL** - 관계형 데이터베이스
- **Gradle** - 빌드 도구

### 주요 라이브러리
- **Lombok** - 보일러플레이트 코드 제거
- **JJWT** - JWT 토큰 생성/검증
- **SpringDoc OpenAPI** - Swagger UI API 문서화
- **Jakarta Validation** - 요청 데이터 검증

---

## 📁 프로젝트 구조

```
src/main/java/me/jiny/boom/
├── config/              # 설정 클래스
│   ├── SecurityConfig.java          # Spring Security 설정
│   ├── JwtAuthenticationFilter.java # JWT 인증 필터
│   └── SwaggerConfig.java           # Swagger 설정
├── controller/          # REST API 컨트롤러
│   ├── AuthController.java
│   ├── UserController.java
│   ├── CardController.java
│   ├── CategoryController.java
│   ├── KeywordController.java
│   ├── CollectionController.java
│   ├── BoomUPController.java
│   └── docs/            # Swagger 문서 인터페이스
├── service/              # 비즈니스 로직
│   ├── AuthService.java
│   ├── UserService.java
│   ├── CardService.java
│   ├── CategoryService.java
│   ├── KeywordService.java
│   ├── CollectionService.java
│   └── BoomUPService.java
├── domain/               # 도메인 모델
│   ├── entity/          # JPA 엔티티
│   ├── repository/       # JPA 리포지토리
│   └── TasteType.java   # 페르소나 유형 enum
├── dto/                  # 데이터 전송 객체
│   ├── request/         # 요청 DTO
│   ├── response/        # 응답 DTO
│   └── mapper/          # DTO 매퍼
├── util/                 # 유틸리티 클래스
│   ├── JwtUtil.java              # JWT 유틸리티
│   └── TasteAnalysisUtil.java   # 취향 분석 유틸리티
└── exception/            # 예외 처리
    └── GlobalExceptionHandler.java
```

---

## 🎯 주요 기능

### 1. 인증 및 사용자 관리
- JWT 기반 인증 (Access Token + Refresh Token)
- 회원가입, 로그인, 토큰 갱신
- 사용자 프로필 조회 및 수정

### 2. 카드 관리
- 카드 생성, 수정, 삭제
- 카드 목록 조회 (내 카드, 사용자별, 최신순, 인기순)
- **PAEF 점수 기반 추천 카드** (유사도 계산)

### 3. 취향 분석
- **PAEF 점수 계산**: 사용자의 모든 카드의 카테고리 점수를 boomLevel 가중치로 평균 계산
- **취향 유형 분류**: 16가지 페르소나 유형 자동 분류 (SCTL, SCTF, SCEL, SCEF, SPTL, SPTF, SPEL, SPEF, ACTL, ACTF, ACEL, ACEF, APTL, APTF, APEL, APEF)
- **페르소나 유형 정보**: 각 유형별 코드, 이름, 설명 제공
- **취향 통계**: TOP 5 카테고리, TOP 5 키워드, 월별 통계
- **취향 유사도**: 유클리드 거리 기반 사용자 간 유사도 계산

### 4. 수집 및 BoomUP
- 카드 수집 기능
- BoomUP (좋아요) 기능
- 수집함 조회

### 5. 카테고리 및 키워드
- 카테고리 조회 (전체, 소분류)
- 키워드 조회 (전체, 타입별)

---

## 🗄 데이터베이스

### 주요 엔티티

- **User**: 사용자 정보
- **Card**: 카드 정보
- **Category**: 카테고리 (PAEF 점수 포함)
- **SubCategory**: 소분류
- **Keyword**: 키워드
- **CardCollection**: 카드 수집 정보
- **BoomUP**: BoomUP 정보
- **UserScore**: 사용자 취향 점수
- **UserSimilarity**: 사용자 간 유사도
- **UserKeywordStats**: 사용자 키워드 통계
- **MonthlyStatistics**: 월별 통계

---

## 🔐 보안

### 인증 방식

- **JWT (JSON Web Token)** 기반 인증
- Access Token: 짧은 유효기간 (1일)
- Refresh Token: 긴 유효기간 (7일)

### 보안 설정

- Spring Security를 통한 엔드포인트 보호
- 비밀번호는 BCrypt로 해싱
- CORS 설정 (필요시 추가)

---

## 📝 주요 알고리즘

### PAEF 점수 계산

```java
// boomLevel을 가중치로 사용한 가중 평균
가중합 = Σ(카테고리_점수 × boomLevel)
평균 = 가중합 / Σ(boomLevel)
```

### 취향 유형 분류 (페르소나 유형)

각 PAEF 점수를 기준점(5점)과 비교하여 16가지 페르소나 유형으로 분류합니다:

```java
// 점수 기준점: 5점
// 매핑 규칙:
// - Activity: (5점 미만) → S, (5점 이상) → A
// - Productivity: (5점 미만) → C, (5점 이상) → P
// - Emotional: (5점 미만) → T, (5점 이상) → E
// - Focus: (5점 미만) → L, (5점 이상) → F

유형코드 = (A점수 >= 5 ? "A" : "S") + 
          (P점수 >= 5 ? "P" : "C") + 
          (E점수 >= 5 ? "E" : "T") + 
          (F점수 >= 5 ? "F" : "L")
```

**16가지 페르소나 유형:**
- **SCTL**: 효율 추구 스마트 유저
- **SCTF**: 지적인 탐구 생활자
- **SCEL**: 방구석 힐링 요정
- **SCEF**: 감성에 잠긴 철학자
- **SPTL**: 방구석 크리에이터
- **SPTF**: 집념의 장인
- **SPEL**: 트렌드세터 몽상가
- **SPEF**: 고독한 예술가
- **ACTL**: 발로 뛰는 정보통
- **ACTF**: 필드 위의 전략가
- **ACEL**: 자유로운 영혼의 나그네
- **ACEF**: 자연 속의 사색가
- **APTL**: 감각적인 행동대장
- **APTF**: 끈기 있는 개척자
- **APEL**: 열정적인 무대 체질
- **APEF**: 육각형 갓생 러너

각 유형은 `TasteType` enum으로 관리되며, 코드, 이름, 설명 정보를 포함합니다.

### 취향 유사도 계산

```java
// 유클리드 거리 기반 유사도
거리 = √(Σ(점수_차이²))
유사도 = 1 - (거리 / 최대거리)
```

### 추천 카드 알고리즘

1. 현재 사용자의 APEF 점수 계산
2. 모든 카드의 카테고리 APEF 점수와 유사도 계산
3. 유사도 높은 순으로 정렬
4. 같은 유사도면 최신순으로 정렬
5. 상위 4개 반환

