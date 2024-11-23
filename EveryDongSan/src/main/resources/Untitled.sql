-- create schema dongsan; 

use dongsan;

CREATE TABLE `home` (
    `no` INT PRIMARY KEY AUTO_INCREMENT,        -- 이름
    `name` VARCHAR(100) NOT NULL,               -- 이름
    `latitude` DECIMAL(9, 6),                   -- 위도
    `longitude` DECIMAL(9, 6),                  -- 경도
    `area` DECIMAL(10, 2),                      -- 면적
    `category` VARCHAR(50),                     -- 분류 (예: 아파트, 원룸, 오피스텔)
    `price` VARCHAR(100),                                -- 가격
    `rentalType` VARCHAR(10),                   -- 전월세 여부
    `memberId` VARCHAR(50),                     -- 부동산업자(브로커) ID (member 테이블 참조)
    `schoolId` INT,
    `score` INT                         -- 학교 ID (외래키)
   
);


CREATE TABLE `member` (
  `id` VARCHAR(50) PRIMARY KEY,
  `pw` VARCHAR(255) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `emailId` VARCHAR(50) NOT NULL,
  `emailDomainId` INT NOT NULL,
  `tel` VARCHAR(15) NOT NULL,
  `role` VARCHAR(15) NOT NULL,
  `schoolId` INT
);

CREATE TABLE `email_domain` (
  `domainId` INT PRIMARY KEY AUTO_INCREMENT,
  `domain` VARCHAR(255) NOT NULL
);

CREATE TABLE `region` (
  `regionId` INT PRIMARY KEY AUTO_INCREMENT,
  `region` VARCHAR(100) NOT NULL
);

CREATE TABLE `school` (
  `schoolId` INT PRIMARY KEY AUTO_INCREMENT,
  `school` VARCHAR(50) NOT NULL
);

CREATE TABLE `review` (
  `articleNo` INT PRIMARY KEY AUTO_INCREMENT,
  `memberId` VARCHAR(50) NOT NULL,
  `homeNo` INT NOT NULL,
  `content` VARCHAR(2000),
  `reviewScore` INT DEFAULT 0
);

CREATE TABLE `community` (
  `articleNo` INT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(50),
  `memberId` VARCHAR(50),
  `homeNo` INT,
  `content` VARCHAR(2000),
  `hit` INT DEFAULT 0
);

CREATE TABLE `chat` (
  `chatId` INT PRIMARY KEY AUTO_INCREMENT,
  `homeNo` INT NOT NULL,
  `memberIdFrom` VARCHAR(50) NOT NULL,
  `memberIdTo` VARCHAR(50) NOT NULL,
  `createdAt` DATETIME DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `chat_message` (
  `messageId` INT PRIMARY KEY AUTO_INCREMENT,
  `chatId` INT NOT NULL,
  `senderId` VARCHAR(50) NOT NULL,
  `content` TEXT NOT NULL,
  `createdAt` DATETIME DEFAULT (CURRENT_TIMESTAMP)
);

-- DELIMITER 변경 (기본 세미콜론을 다른 문자로 변경)
DELIMITER $$

-- 트리거 정의
CREATE TRIGGER update_home_score_after_insert
AFTER INSERT ON review
FOR EACH ROW
BEGIN
    UPDATE home
    SET score = (
        SELECT COALESCE(AVG(r.reviewScore), 0)
        FROM review r
        WHERE r.homeNo = NEW.homeNo
    )
    WHERE no = NEW.homeNo;
END$$

CREATE TRIGGER update_home_score_after_update
AFTER UPDATE ON review
FOR EACH ROW
BEGIN
    UPDATE home
    SET score = (
        SELECT COALESCE(AVG(r.reviewScore), 0)
        FROM review r
        WHERE r.homeNo = NEW.homeNo
    )
    WHERE no = NEW.homeNo;
END$$

-- DELIMITER 원래대로 세미콜론으로 변경
DELIMITER ;

