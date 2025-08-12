# Identity Service

사용자 인증 및 신원 관리를 위한 Spring Boot 기반 백엔드 서비스입니다.

## 📋 주요 기능

### 1. 사용자 관리 (User Management)
- 사용자 계정 생성, 조회, 수정, 삭제 (CRUD)
- 사용자 프로필 관리
- 사용자 권한 및 역할 관리
- 사용자 상태 관리 (활성/비활성/정지)

### 2. 인증 (Authentication)
- 사용자 로그인/로그아웃
- JWT 토큰 기반 인증
- 토큰 갱신 (Refresh Token)
- 비밀번호 암호화 및 검증
- 계정 잠금 및 해제

### 3. 권한 관리 (Authorization)
- 역할 기반 접근 제어 (RBAC)
- 리소스별 권한 설정
- API 엔드포인트 보안

### 4. 보안 기능
- 비밀번호 정책 적용
- 계정 보안 이벤트 로깅
- 브루트 포스 공격 방지
- 세션 관리

## 🏗️ 아키텍처 구조

### Clean Architecture (헥사고날 아키텍처)
```
src/main/java/com/hammefatal/digitalworkshop/identity_service/
├── adapter/
│   ├── in/web/          # 웹 컨트롤러 (REST API)
│   │   ├── AuthController.java
│   │   └── UserController.java
│   └── out/persistence/ # 데이터베이스 어댑터
├── application/
│   ├── port/
│   │   ├── in/          # 인바운드 포트 (유스케이스 인터페이스)
│   │   └── out/         # 아웃바운드 포트 (리포지토리 인터페이스)
│   └── service/         # 비즈니스 로직 서비스
└── domain/              # 도메인 모델
```

## 📊 데이터베이스 설계

### 데이터베이스: `identity_service_db`

#### 1. Core Tables (핵심 테이블)

##### `users` - 사용자 기본 정보
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 사용자 고유 ID |
| `username` | VARCHAR(50) | 사용자명 (고유) |
| `email` | VARCHAR(100) | 이메일 주소 (고유) |
| `password_hash` | VARCHAR(255) | 암호화된 비밀번호 |
| `first_name` | VARCHAR(50) | 이름 |
| `last_name` | VARCHAR(50) | 성 |
| `phone_number` | VARCHAR(20) | 전화번호 |
| `date_of_birth` | DATE | 생년월일 |
| `profile_image_url` | VARCHAR(500) | 프로필 이미지 URL |
| `is_email_verified` | BOOLEAN | 이메일 인증 여부 |
| `is_phone_verified` | BOOLEAN | 전화번호 인증 여부 |
| `account_status` | ENUM | 계정 상태 (ACTIVE, INACTIVE, SUSPENDED, LOCKED) |
| `failed_login_attempts` | INT | 로그인 실패 횟수 |
| `last_login_at` | TIMESTAMP | 마지막 로그인 시간 |
| `password_changed_at` | TIMESTAMP | 비밀번호 변경 시간 |
| `created_at` | TIMESTAMP | 생성 시간 |
| `updated_at` | TIMESTAMP | 수정 시간 |
| `created_by` | BIGINT (FK) | 생성자 ID |
| `updated_by` | BIGINT (FK) | 수정자 ID |

##### `roles` - 역할 정의
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 역할 고유 ID |
| `name` | VARCHAR(50) | 역할명 (고유, 예: ADMIN, USER) |
| `display_name` | VARCHAR(100) | 역할 표시명 |
| `description` | TEXT | 역할 설명 |
| `is_system_role` | BOOLEAN | 시스템 역할 여부 |
| `is_active` | BOOLEAN | 활성화 여부 |
| `created_at` | TIMESTAMP | 생성 시간 |
| `updated_at` | TIMESTAMP | 수정 시간 |
| `created_by` | BIGINT (FK) | 생성자 ID |
| `updated_by` | BIGINT (FK) | 수정자 ID |

##### `permissions` - 권한 정의
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 권한 고유 ID |
| `name` | VARCHAR(100) | 권한명 (고유) |
| `display_name` | VARCHAR(100) | 권한 표시명 |
| `description` | TEXT | 권한 설명 |
| `resource` | VARCHAR(50) | 리소스 (예: USER, ROLE) |
| `action` | VARCHAR(50) | 액션 (예: READ, WRITE, DELETE) |
| `is_system_permission` | BOOLEAN | 시스템 권한 여부 |
| `is_active` | BOOLEAN | 활성화 여부 |
| `created_at` | TIMESTAMP | 생성 시간 |
| `updated_at` | TIMESTAMP | 수정 시간 |
| `created_by` | BIGINT (FK) | 생성자 ID |
| `updated_by` | BIGINT (FK) | 수정자 ID |

#### 2. Mapping Tables (매핑 테이블)

##### `user_roles` - 사용자-역할 매핑
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 매핑 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID |
| `role_id` | BIGINT (FK) | 역할 ID |
| `granted_at` | TIMESTAMP | 권한 부여 시간 |
| `granted_by` | BIGINT (FK) | 권한 부여자 ID |
| `expires_at` | TIMESTAMP | 만료 시간 (NULL이면 무기한) |
| `is_active` | BOOLEAN | 활성화 여부 |

##### `role_permissions` - 역할-권한 매핑
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 매핑 고유 ID |
| `role_id` | BIGINT (FK) | 역할 ID |
| `permission_id` | BIGINT (FK) | 권한 ID |
| `granted_at` | TIMESTAMP | 권한 부여 시간 |
| `granted_by` | BIGINT (FK) | 권한 부여자 ID |

#### 3. Security & Session Tables (보안 및 세션 테이블)

##### `user_sessions` - 사용자 세션 관리
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 세션 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID |
| `session_token` | VARCHAR(255) | JWT 액세스 토큰 |
| `refresh_token` | VARCHAR(255) | JWT 리프레시 토큰 |
| `device_info` | VARCHAR(500) | 디바이스 정보 |
| `ip_address` | VARCHAR(45) | IP 주소 |
| `user_agent` | TEXT | 사용자 에이전트 |
| `is_active` | BOOLEAN | 활성화 여부 |
| `created_at` | TIMESTAMP | 생성 시간 |
| `updated_at` | TIMESTAMP | 수정 시간 |
| `expires_at` | TIMESTAMP | 만료 시간 |
| `last_accessed_at` | TIMESTAMP | 마지막 접근 시간 |

##### `password_reset_tokens` - 비밀번호 재설정 토큰
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 토큰 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID |
| `token` | VARCHAR(255) | 재설정 토큰 |
| `expires_at` | TIMESTAMP | 만료 시간 |
| `is_used` | BOOLEAN | 사용 여부 |
| `used_at` | TIMESTAMP | 사용 시간 |
| `created_at` | TIMESTAMP | 생성 시간 |

##### `email_verification_tokens` - 이메일 인증 토큰
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 토큰 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID |
| `token` | VARCHAR(255) | 인증 토큰 |
| `email` | VARCHAR(100) | 인증할 이메일 |
| `expires_at` | TIMESTAMP | 만료 시간 |
| `is_verified` | BOOLEAN | 인증 여부 |
| `verified_at` | TIMESTAMP | 인증 시간 |
| `created_at` | TIMESTAMP | 생성 시간 |

#### 4. Logging & Audit Tables (로깅 및 감사 테이블)

##### `security_logs` - 보안 이벤트 로그
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 로그 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID (NULL 허용) |
| `event_type` | VARCHAR(50) | 이벤트 타입 (LOGIN, LOGOUT, 등) |
| `event_description` | TEXT | 이벤트 설명 |
| `ip_address` | VARCHAR(45) | IP 주소 |
| `user_agent` | TEXT | 사용자 에이전트 |
| `session_id` | VARCHAR(255) | 세션 ID |
| `success` | BOOLEAN | 성공 여부 |
| `additional_data` | JSON | 추가 데이터 |
| `created_at` | TIMESTAMP | 생성 시간 |

##### `user_audit_logs` - 사용자 정보 변경 이력
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 감사 로그 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID |
| `field_name` | VARCHAR(50) | 변경된 필드명 |
| `old_value` | TEXT | 이전 값 |
| `new_value` | TEXT | 새로운 값 |
| `changed_by` | BIGINT (FK) | 변경자 ID |
| `change_reason` | VARCHAR(200) | 변경 사유 |
| `created_at` | TIMESTAMP | 생성 시간 |

#### 5. Optional Tables (선택적 테이블)

##### `user_addresses` - 사용자 주소 정보
| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| `id` | BIGINT (PK) | 주소 고유 ID |
| `user_id` | BIGINT (FK) | 사용자 ID |
| `address_type` | ENUM | 주소 타입 (HOME, WORK, OTHER) |
| `street_address` | VARCHAR(200) | 도로명 주소 |
| `city` | VARCHAR(100) | 도시 |
| `state_province` | VARCHAR(100) | 주/도 |
| `postal_code` | VARCHAR(20) | 우편번호 |
| `country` | VARCHAR(100) | 국가 |
| `is_primary` | BOOLEAN | 기본 주소 여부 |
| `is_active` | BOOLEAN | 활성화 여부 |
| `created_at` | TIMESTAMP | 생성 시간 |
| `updated_at` | TIMESTAMP | 수정 시간 |

### 데이터베이스 인덱스 및 제약조건

#### 주요 인덱스
- **성능 최적화**: username, email, account_status 등 자주 검색되는 필드
- **보안 로그**: event_type, ip_address, created_at으로 빠른 조회
- **세션 관리**: session_token, refresh_token으로 빠른 토큰 검증

#### 외래 키 제약조건
- **CASCADE DELETE**: 사용자 삭제 시 관련 세션, 토큰 등 자동 삭제
- **SET NULL**: 참조하는 사용자 삭제 시 로그 유지하되 NULL 처리

## 🛠️ 기술 스택

- **Framework**: Spring Boot 3.5.4
- **Java Version**: 21
- **Database**: JPA/Hibernate
- **Security**: Spring Security + JWT
- **API Documentation**: OpenAPI 3 (Swagger)
- **Build Tool**: Gradle
- **Container**: Docker

## 🚀 설치 및 실행

### 1. 로컬 개발 환경
```bash
# 프로젝트 클론
git clone <repository-url>
cd identity-service

# 애플리케이션 실행
./gradlew bootRun
```

### 2. Docker 실행
```bash
# Docker 이미지 빌드
./gradlew docker

# Docker Compose로 실행
docker-compose up -d
```

### 3. 데이터베이스 초기화 (PostgreSQL)
```bash
# PostgreSQL 데이터베이스 생성 (superuser로 실행)
createdb identity_service_db

# 스키마 생성
psql -U postgres -d identity_service_db -f db_init_scripts/01_schema.sql

# 초기 데이터 생성 (선택적)
psql -U postgres -d identity_service_db -f db_init_scripts/02_data.sql
```

## 📝 API 엔드포인트

### 인증 관련
- `POST /api/v1/auth/login` - 사용자 로그인
- `POST /api/v1/auth/logout` - 사용자 로그아웃
- `POST /api/v1/auth/refresh` - 토큰 갱신
- `POST /api/v1/auth/register` - 사용자 등록

### 사용자 관리 (구현 완료 ✅)
- `GET /api/v1/users` - 전체 사용자 목록 조회
- `GET /api/v1/users/{id}` - ID로 특정 사용자 조회
- `GET /api/v1/users/username/{username}` - 사용자명으로 조회
- `GET /api/v1/users/email/{email}` - 이메일로 조회
- `GET /api/v1/users/status/{status}` - 계정 상태별 사용자 목록
- `GET /api/v1/users/search/name?searchTerm=` - 이름으로 사용자 검색
- `GET /api/v1/users/search/email?searchTerm=` - 이메일로 사용자 검색
- `GET /api/v1/users/count` - 전체 사용자 수 조회
- `POST /api/v1/users` - 새 사용자 생성
- `PUT /api/v1/users/{id}` - 사용자 정보 수정
- `PUT /api/v1/users/{id}/profile` - 사용자 프로필 수정
- `PUT /api/v1/users/{id}/status` - 계정 상태 변경
- `PUT /api/v1/users/{id}/password` - 비밀번호 변경
- `PUT /api/v1/users/{id}/soft-delete` - 사용자 소프트 삭제 (비활성화)
- `DELETE /api/v1/users/{id}` - 사용자 완전 삭제
- `DELETE /api/v1/users/username/{username}` - 사용자명으로 삭제
- `DELETE /api/v1/users/email/{email}` - 이메일로 삭제

## 🔧 구현된 기능 상세

### 사용자 관리 기능 (User Management)

#### 🏗️ 아키텍처 구조
Clean Architecture 패턴을 적용하여 다음과 같이 구현되었습니다:

```
📁 domain/
  └── User.java                    # 사용자 도메인 모델

📁 application/
  ├── port/in/                     # 인바운드 포트 (유스케이스)
  │   ├── CreateUserUseCase.java   # 사용자 생성 유스케이스
  │   ├── GetUserUseCase.java      # 사용자 조회 유스케이스
  │   ├── UpdateUserUseCase.java   # 사용자 수정 유스케이스
  │   └── DeleteUserUseCase.java   # 사용자 삭제 유스케이스
  ├── port/out/
  │   └── UserRepository.java      # 아웃바운드 포트 (리포지토리 인터페이스)
  └── service/
      └── UserService.java         # 비즈니스 로직 구현

📁 adapter/
  ├── in/web/
  │   └── UserController.java      # REST API 컨트롤러
  └── out/persistence/
      ├── UserEntity.java          # JPA 엔티티
      ├── UserJpaRepository.java   # Spring Data JPA 리포지토리
      ├── UserRepositoryImpl.java  # 리포지토리 구현체
      └── UserMapper.java          # 도메인-엔티티 매핑
```

#### 🔍 주요 구현된 기능들

1. **사용자 생성 (Create User)**
   - 사용자명, 이메일 중복 검증
   - 필수 정보 유효성 검사
   - 비밀번호 해싱 (추후 BCrypt 적용 예정)

2. **사용자 조회 (Get User)**
   - ID, 사용자명, 이메일로 개별 조회
   - 전체 사용자 목록 조회
   - 계정 상태별 필터링
   - 이름/이메일 검색 기능

3. **사용자 수정 (Update User)**
   - 기본 정보 수정 (이름, 전화번호, 생년월일 등)
   - 프로필 정보 업데이트
   - 계정 상태 변경 (활성/비활성/정지/잠금)
   - 비밀번호 변경 (현재 비밀번호 확인 포함)

4. **사용자 삭제 (Delete User)**
   - 완전 삭제 (Hard Delete)
   - 소프트 삭제 (계정 비활성화)
   - ID, 사용자명, 이메일로 삭제

#### 📊 지원되는 데이터 타입
- 계정 상태: `ACTIVE`, `INACTIVE`, `SUSPENDED`, `LOCKED`
- 날짜/시간: 모든 timestamp 필드 자동 관리
- 검증: 이메일/전화번호 인증 상태 추적
- 보안: 로그인 실패 횟수, 마지막 로그인 시간 추적

#### 🛡️ 보안 기능
- 사용자명/이메일 유니크 제약조건
- 계정 잠금 및 해제 기능
- 로그인 실패 횟수 추적
- 비밀번호 변경 이력 관리

#### 📝 API 요청/응답 예시

**사용자 생성 요청:**
```json
POST /api/v1/users
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+821234567890"
}
```

**계정 상태 변경 요청:**
```json
PUT /api/v1/users/1/status
{
  "accountStatus": "SUSPENDED"
}
```

**비밀번호 변경 요청:**
```json
PUT /api/v1/users/1/password
{
  "currentPassword": "oldPassword",
  "newPassword": "newSecurePassword123"
}
```

## 📖 API 문서

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## 🔧 환경 설정

### application.properties
```properties
# 서버 설정
server.port=8081

# 데이터베이스 설정 (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/identity_service_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA 설정
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# JWT 설정 (추후 구현 예정)
jwt.secret=your-secret-key
jwt.expiration=86400000

# Swagger/OpenAPI 설정
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

## 🧪 테스트

```bash
# 단위 테스트 실행
./gradlew test

# 통합 테스트 실행
./gradlew integrationTest
```

## 📄 라이선스

MIT License
