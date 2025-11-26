# Boom! API 명세서 (간단 버전)

**기본 URL**: `{API_URL}/api`

---

## 인증 (Auth)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/auth/signup` | 회원가입 | ❌ |
| POST | `/auth/login` | 로그인 | ❌ |
| POST | `/auth/refresh` | 토큰 갱신 | ❌ |

---

## 사용자 (User)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/users/me` | 내 프로필 조회 | ✅ |
| PUT | `/users/me` | 프로필 수정 | ✅ |
| GET | `/users/{userId}` | 다른 사용자 프로필 조회 | ❌ |
| GET | `/users/{userId}/similarity` | 취향 유사도 조회 | ✅ |
| GET | `/users/{userId}/statistics` | 취향 통계 조회 | ❌ |

---

## 카드 (Card)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/cards` | 카드 생성 | ✅ |
| GET | `/cards/{cardId}` | 카드 상세 조회 | ⚠️ |
| PUT | `/cards/{cardId}` | 카드 수정 | ✅ |
| DELETE | `/cards/{cardId}` | 카드 삭제 | ✅ |
| GET | `/cards/me` | 내 카드 목록 조회 | ✅ |
| GET | `/cards/users/{userId}` | 사용자 카드 목록 조회 | ❌ |
| GET | `/cards/explore/recent` | 최근 카드 조회 | ❌ |
| GET | `/cards/explore/ranking` | 인기 카드 랭킹 | ❌ |
| GET | `/cards/explore/recommendations` | 추천 카드 | ✅ |
| GET | `/cards/{cardId}/download` | 카드 이미지 다운로드 | ✅ |

**⚠️**: 선택적 인증 (토큰이 있으면 추가 정보 포함)

---

## 카테고리 (Category)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/categories` | 전체 카테고리 조회 | ❌ |
| GET | `/categories/{categoryId}/subcategories` | 카테고리별 소분류 조회 | ❌ |

---

## 키워드 (Keyword)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/keywords` | 전체 키워드 조회 | ❌ |
| GET | `/keywords/type/{type}` | 타입별 키워드 조회 | ❌ |

**타입**: `FEELING`, `ACTION`, `TENDENCY`

---

## 수집 (Collection)

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/collections/cards/{cardId}` | 카드 수집 | ✅ |
| DELETE | `/collections/cards/{cardId}` | 카드 수집 취소 | ✅ |
| GET | `/collections/me` | 내 수집함 조회 | ✅ |

---

## BoomUP

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/boomups/cards/{cardId}` | BoomUP 추가 | ✅ |
| DELETE | `/boomups/cards/{cardId}` | BoomUP 취소 | ✅ |

---

## 공통 사항

### 응답 형식

모든 API 응답은 다음과 같은 형식을 따릅니다:

```json
{
  "success": boolean,
  "message": string,
  "data": T
}
```

### 인증

인증이 필요한 API는 `Authorization` 헤더에 Bearer 토큰을 포함해야 합니다:

```http
Authorization: Bearer {accessToken}
```

### HTTP 상태 코드

| 코드 | 설명 |
|------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 |
| 401 | 인증 필요 |
| 403 | 권한 없음 |
| 404 | 리소스를 찾을 수 없음 |
| 500 | 서버 오류 |

---

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

