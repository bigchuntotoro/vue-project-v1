# Vue + Spring Boot + MyBatis + MariaDB 게시판

## 구성
```
board-project/
├── backend/    Spring Boot 3 + MyBatis REST API (포트 8080)
├── frontend/   Vue 3 + Vite SPA (포트 5173)
└── database/   MariaDB 스키마 (schema.sql)
```

## 기능
- 게시글 목록 조회 (제목/작성자/내용 검색, 페이징)
- 게시글 상세 조회 (조회수 자동 증가)
- 게시글 등록 / 수정 / 삭제
- 유효성 검사 (제목/작성자/내용 필수)

## 실행 순서

### 1. MariaDB 준비
MariaDB가 설치되어 있어야 합니다. 아래 명령으로 DB, 계정, 테이블, 샘플 데이터를 생성합니다.
```bash
mysql -u root -p < database/schema.sql
```
(schema.sql 안에서 `board_db` DB와 `board_user`/`board_pass` 계정을 생성합니다. 운영 환경에서는 반드시 비밀번호를 바꾸세요.)

### 2. 백엔드 실행
`backend/src/main/resources/application.yml`에서 DB 접속 정보를 확인/수정한 뒤 실행합니다.
```bash
cd backend
./mvnw spring-boot:run
```
(mvnw가 없다면 `mvn spring-boot:run` 또는 IDE에서 `BoardApplication` 실행)
API는 `http://localhost:8080/api/boards` 로 노출됩니다.

### 3. 프론트엔드 실행
```bash
cd frontend
npm install
npm run dev
```
브라우저에서 `http://localhost:5173` 접속. Vite 프록시 설정으로 `/api` 요청이 자동으로 8080 백엔드로 전달됩니다.

## API 명세

| Method | URL                | 설명                              |
|--------|--------------------|-----------------------------------|
| GET    | /api/boards         | 목록 조회 (page, size, searchType, keyword) |
| GET    | /api/boards/{id}    | 상세 조회 (조회수 +1)             |
| POST   | /api/boards         | 등록 (title, writer, content)     |
| PUT    | /api/boards/{id}    | 수정                               |
| DELETE | /api/boards/{id}    | 삭제                               |

## 배포 시 참고
- `application.yml`의 DB 계정/비밀번호는 환경변수나 별도 프로파일로 분리 권장
- 프론트엔드 빌드 결과(`npm run build`)를 Spring Boot의 `static` 폴더에 넣으면 단일 서버로 배포 가능
- CORS 설정(`WebConfig`)은 개발용이며, 배포 시 운영 도메인으로 변경 필요
