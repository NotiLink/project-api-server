# 🎯 Key-Catch: API Server (Backend)

> **"흩어진 정보를 하나로 잇다, Key-Catch"**

**Key-Catch**는 정보 과잉 시대에 사용자가 원하는 키워드만 등록하면 24시간 웹을 감시하여 알림을 제공하는 **개인화 정보 구독 서비스**입니다.
본 리포지토리는 서비스의 중추적인 역할을 담당하는 **Core API Server**의 소스 코드를 담고 있습니다.

## 🏗️ System Architecture

본 프로젝트는 **안정성(Stability)**과 **확장성(Scalability)**을 최우선으로 고려하여 **MSA (Microservices Architecture)** 구조를 채택했습니다.

### 🔹 Service Architecture

*(여기에 아키텍처 다이어그램 이미지를 삽입하세요)*

### 🔹 Database ERD

**API Server, Crawler, Notifier** 3개의 서버가 데이터를 공유하는 모델입니다.
![Key-Catch ERD](img/erd.png)

## 🔥 Key Features (핵심 구현 내용)

### 1. 보안 아키텍처 (Security)

* **JWT 기반 Stateless 인증**:
* `JwtAuthenticationFilter`를 커스텀 구현하여 요청 헤더의 토큰 유효성을 검증합니다.
* DB 조회 없이 토큰 자체의 **서명(Signature)**만으로 인증을 처리하여 서버 부하를 최소화하고 성능을 최적화했습니다.


* **HTTPS (SSL) 적용**:
* AWS EC2 앞단에 **Nginx**를 리버스 프록시로 배치하고 Let's Encrypt를 연동하여 SSL 인증서를 적용했습니다.
* 프론트엔드(Vercel/HTTPS)와 백엔드(EC2/HTTP) 통신 시 발생하는 **Mixed Content 문제를 원천 차단**했습니다.



### 2. 장애 격리 (Fault Isolation) 설계

* **공용 데이터베이스 패턴 (Shared Database)**:
* API 서버와 크롤링 서버 간의 직접적인 통신(HTTP/RPC)을 배제하고 DB를 통해 데이터를 교환하도록 설계했습니다.
* 외부 사이트의 변경이나 타임아웃으로 크롤러가 중단되더라도, 사용자가 이용하는 **API 서버(로그인, 키워드 조회 등)는 100% 정상 가동되는 고가용성**을 확보했습니다.



### 3. 데이터 무결성 및 매핑

* **Boolean 필드 매핑 최적화**:
* JSON 파싱 라이브러리(Jackson)와 Lombok 간의 boolean 필드명 충돌 이슈를 해결하기 위해 Wrapper Class(`Boolean`) 도입 및 `@JsonProperty`를 적용하여 데이터 손실을 방지했습니다.



## 🚀 Trouble Shooting (문제 해결 경험)

프로젝트 진행 중 발생한 기술적 이슈와 해결 과정입니다.

### 1. JSON 필드 매핑 이슈 (데이터 초기화 현상)

* **문제**: 프론트엔드에서 `isNotifyEnabled: true` 값을 보냈으나, 백엔드 DB에는 계속 `false`로 저장되는 현상 발생.
* **원인**: Java의 `boolean` 원시 타입 변수명이 `is`로 시작할 때, Lombok이 생성하는 Getter(`isNotifyEnabled()`)와 Jackson 라이브러리의 파싱 규칙(`notifyEnabled`로 인식)이 엇갈려 매핑에 실패함.
* **해결**:
1. DTO 필드 타입을 `boolean` (Primitive)에서 **`Boolean` (Wrapper Class)**으로 변경하여 Lombok이 `getIsNotifyEnabled()`를 생성하도록 유도.
2. `@JsonProperty("isNotifyEnabled")` 어노테이션을 명시하여 JSON 키값을 강제.


* **결과**: 데이터 바인딩 성공률 100% 달성 및 프론트엔드와의 명확한 인터페이스 규약 확립.

### 2. CORS 및 Preflight(OPTIONS) 403 에러

* **문제**: 로그인(POST) 요청은 성공하나, 삭제(DELETE)나 수정(PATCH) 요청 시 **403 Forbidden** 에러 발생.
* **원인**: Spring Security 필터 체인에서 브라우저가 보내는 예비 요청인 **Preflight(OPTIONS 메서드)**에 대한 허용 설정이 인증 필터(`authenticated()`)보다 늦게 처리되거나 누락되어 차단됨.
* **해결**:
1. `SecurityConfig`에서 `HttpMethod.OPTIONS`에 대한 요청을 필터 체인 최상단에 배치하여 `permitAll()`로 설정.
2. `CorsConfigurationSource` 설정에 `DELETE`, `PATCH` 메서드를 명시적으로 허용 리스트에 추가.



## 📂 Project Structure

```bash
com.chosun.demoversion
│
├── DemoversionApplication.java        # Main Execution File
│
├── config                             # Configuration (Security, Docs, Utils)
│   ├── SecurityConfig.java            # Spring Security & CORS Setup
│   ├── SwaggerConfig.java             # Swagger API Documentation
│   ├── JwtTokenProvider.java          # JWT Token Generation & Validation
│   └── JwtAuthenticationFilter.java   # JWT Auth Filter
│
├── controller                         # API Layer (Request/Response)
│   ├── AuthController.java            # Login, Signup
│   ├── KeywordController.java         # Keyword Operations
│   └── NotificationController.java    # Notification Operations
│
├── domain                             # Entity Layer (DB Mapping)
│   ├── User.java
│   ├── Keyword.java
│   └── Notification.java
│
├── dto                                # DTO (Data Transfer Object)
│   ├── SignupRequest.java
│   ├── LoginRequest.java
│   ├── TokenResponse.java
│   ├── KeywordRequest.java / Response.java
│   └── NotificationResponse.java / UnreadCountResponse.java
│
├── repository                         # JPA Repository Layer
│   ├── UserRepository.java
│   ├── KeywordRepository.java
│   └── NotificationRepository.java
│
└── service                            # Business Logic Layer
    ├── AuthService.java
    ├── KeywordService.java
    └── NotificationService.java

