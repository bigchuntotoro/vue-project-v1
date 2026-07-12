-- MariaDB 게시판 스키마
CREATE DATABASE IF NOT EXISTS board_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER IF NOT EXISTS 'board_user'@'%' IDENTIFIED BY 'board_pass';
GRANT ALL PRIVILEGES ON board_db.* TO 'board_user'@'%';
FLUSH PRIVILEGES;

USE board_db;

CREATE TABLE IF NOT EXISTS board (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200)  NOT NULL,
    writer        VARCHAR(50)   NOT NULL,
    content       TEXT          NOT NULL,
    view_count    INT           NOT NULL DEFAULT 0,
    created_date  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 샘플 데이터
INSERT INTO board (title, writer, content, view_count) VALUES
('공지사항입니다', '관리자', '게시판 이용 안내입니다. 자유롭게 글을 남겨주세요.', 10),
('첫 번째 게시글', '홍길동', '안녕하세요! 반갑습니다.', 3),
('Vue와 Spring Boot 연동 테스트', '개발자', 'MyBatis와 MariaDB로 만든 게시판 예제입니다.', 7);
