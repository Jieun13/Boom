# Boom! Backend Server

**'요즘 나의 붐은?' 취향 아카이브 서비스 백엔드**

사용자의 취향을 카드 형태로 기록하고, PAEF(Productivity, Activity, Emotional, Focus) 점수 기반으로 취향을 분석하여 유사한 취향을 가진 사용자와 카드를 추천하는 서비스입니다.


## 🛠 기술 스택

#### Backend
- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Security** - JWT 기반 인증
- **Spring Data JPA** - 데이터베이스 ORM
- **MySQL** - 관계형 데이터베이스
- **Gradle** - 빌드 도구


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
│   └── repository/      # JPA 리포지토리
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


## 📚 API 문서

### API 명세서

프로젝트 루트에 다음 명세서가 포함되어 있습니다:

- **API_SPECIFICATION_SIMPLE.md** - 간단한 API 목록


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
- **취향 유형 분류**: 16가지 취향 유형 자동 분류
- **취향 통계**: TOP 5 카테고리, TOP 5 키워드, 월별 통계
- **취향 유사도**: 유클리드 거리 기반 사용자 간 유사도 계산

### 4. 수집 및 BoomUP
- 카드 수집 기능
- BoomUP (좋아요) 기능
- 수집함 조회

### 5. 카테고리 및 키워드
- 카테고리 조회 (전체, 소분류)
- 키워드 조회 (전체, 타입별)


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


## 📝 주요 알고리즘

###  취향 분석 로직 (Logic Overview)

사용자의 카드 수집 데이터를 기반으로 **PAEF 지수**를 산출하고, 이를 통해 **성향 유형** 및 **타 유저와의 유사도**를 분석합니다.

#### 1. PAEF 점수 산출 (Score Calculation)
사용자가 보유한 카드의 카테고리 점수(P, A, E, F)에 **카드 레벨(boomLevel)** 가중치를 적용하여 평균을 산출합니다.
* **계산 방식:** 가중 평균 (Weighted Average)
* **가중치:** 카드의 `boomLevel` (레벨이 높을수록 취향 점수에 더 큰 영향)
* **공식 요약:** `(∑ 카테고리 점수 × 레벨) / ∑ 총 레벨`

#### 2. 성향 유형 판별 (Type Determination)
산출된 4가지 지수(P, A, E, F)를 **기준점(Threshold)**과 비교하여 16가지 성향 중 하나로 분류합니다.
* **기준점:** **5.0 점**
* **표기 규칙:**
    * **5.0 이상 (High):** 대문자 표기 (예: `P`)
    * **5.0 미만 (Low):** 소문자 표기 (예: `p`)
* **예시:** `P(7.0)`, `a(2.0)`, `e(4.5)`, `F(6.0)` → **"PaeF"** (자연 속의 사색가)

#### 3. 유저 유사도 분석 (Similarity Analysis)
두 유저의 PAEF 점수 차이를 기하학적 거리로 계산하여 유사도(%)를 도출합니다.
* **알고리즘:** 유클리드 거리 (Euclidean Distance)
* **계산 로직:**
    1. 4차원 공간(P, A, E, F)에서 두 점수 간의 거리를 측정
    2. 최대 거리(20.0) 대비 현재 거리의 비율을 역산
    3. **0% (완전 불일치) ~ 100% (완전 일치)** 사이의 값으로 환산

#### 4. 추천 카드 알고리즘

1. 현재 사용자의 PAEF 점수 계산
2. 모든 카드의 카테고리 PAEF 점수와 유사도 계산
3. 유사도 높은 순으로 정렬
4. 같은 유사도면 최신순으로 정렬
5. 상위 4개 반환
