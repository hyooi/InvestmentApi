# Investment API

[![Java CI with Gradle](https://github.com/hyooi/InvestmentApi/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/hyooi/InvestmentApi/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/hyooi/InvestmentApi/branch/master/graph/badge.svg?token=S1ZNMVHSYT)](https://codecov.io/gh/hyooi/InvestmentApi)

---
부동산/신용 투자 서비스 RestAPI(*기본포트: 6549)

## 사용법

1. 앱 실행

```
 ./gradlew bootRun
```

2. API요청

```
# 전체 투자상품 조회API
curl -X GET --location "http://localhost:6549/api/products"

# 투자하기API
curl -X POST --location "http://localhost:6549/api/invest/2/10000" -H "X-USER-ID: 1"

# 나의 투자상품 조회API
curl -X GET --location "http://localhost:6549/api/product" -H "X-USER-ID: 1"
```

3. H2 URL

```
http://localhost:6549/h2
```

## 문제해결 전략 & 분석내용

### API

- Spring Jpa를 이용해 구현하되, 복잡한 SQL은 JPQL을 이용
- 결과값은 CommonResponse DTO로 래핑하여 리턴

### DB스키마

![erd](erd.png)

- 투자상품 테이블(INVESTING_PRODUCT)은 각 투자 상품의 ID, 상품명, 총 투자 모집금액 및 투자 가능 기간 정보를 가지며, 투자상태 테이블(
  INVESTING_STATUS)은 각 투자상품에 투자한 유저ID와 투자금액, 투자일시 정보를 가진다.
- 각 테이블은 일대다 관계를 가진다.

### 기타

- in memory DB를 사용하고 있으므로, 실제로 다수의 서버 및 인스턴스로 동작할 시에는 별도의 DB에 붙여 기동해야 함

## 사용 기술

### Language

- Java11

### Framework / Library

- Spring Boot2
- JPA & Hibernate
- Junit5

### Database

- H2(in-memory)

### Etc

- Jacoco
- Lombok
- Gradle
- Github action