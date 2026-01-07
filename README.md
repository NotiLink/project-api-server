# 🎯 Key-Catch: API Server (Backend)

> **"흩어진 정보를 하나로 잇다, Key-Catch"**

**Key-Catch**는 정보 과잉 시대에 사용자가 원하는 키워드만 등록하면 24시간 웹을 감시하여 알림을 제공하는 **개인화 정보 구독 서비스**입니다.

## 🏗️ System Architecture

본 프로젝트는 **안정성(Stability)** 과 **확장성(Scalability)** 을 최우선으로 고려하여 **MSA (Microservices Architecture)** 구조를 채택했습니다.

### 🔹 Service Architecture

![System Architecture](./img/architecture.png)

### 🔹 Database ERD

**API Server, Crawler, Notifier** 3개의 서버가 데이터를 공유하는 모델입니다.

**User(사용자)** 를 중심으로 **Keyword(구독 정보)** 와 **Notification(알림 발송 내역)** 이 1:N 관계로 설계되어 있습니다.

![Key-Catch ERD](./img/erd.png)

## 🔥 Key Features (핵심 구현 내용)

### 1. 보안 아키텍처 (Security)

* **JWT 기반 Stateless 인증**:
* `JwtAuthenticationFilter`를 커스텀 구현하여 요청 헤더의 토큰 유효성을 검증합니다.
* DB 조회 없이 토큰 자체의 **서명(Signature)** 만으로 인증을 처리하여 서버 부하를 최소화하고 성능을 최적화했습니다.


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

