CREATE TABLE `home` (
  `no` INT PRIMARY KEY AUTO_INCREMENT,
  `memberId` VARCHAR(50) NOT NULL,
  `regionId` INT NOT NULL,
  `score` INT,
  `schoolId` INT
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
  `articleNo` INT PRIMARY KEY,
  `memberId` VARCHAR(50) NOT NULL,
  `homeNo` INT NOT NULL,
  `content` VARCHAR(2000),
  `reviewScore` INT DEFAULT 0,
  `hit` INT DEFAULT 0
);

CREATE TABLE `community` (
  `articleNo` INT PRIMARY KEY,
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

ALTER TABLE `region` ADD FOREIGN KEY (`regionId`) REFERENCES `home` (`regionId`);

ALTER TABLE `school` ADD FOREIGN KEY (`schoolId`) REFERENCES `home` (`schoolId`);

ALTER TABLE `email_domain` ADD FOREIGN KEY (`domainId`) REFERENCES `member` (`emailDomainId`);

ALTER TABLE `school` ADD FOREIGN KEY (`schoolId`) REFERENCES `member` (`schoolId`);

ALTER TABLE `home` ADD FOREIGN KEY (`no`) REFERENCES `review` (`homeNo`);

ALTER TABLE `member` ADD FOREIGN KEY (`id`) REFERENCES `review` (`memberId`);

ALTER TABLE `home` ADD FOREIGN KEY (`no`) REFERENCES `community` (`homeNo`);

ALTER TABLE `member` ADD FOREIGN KEY (`id`) REFERENCES `community` (`memberId`);

ALTER TABLE `home` ADD FOREIGN KEY (`no`) REFERENCES `chat` (`homeNo`);

ALTER TABLE `chat` ADD FOREIGN KEY (`chatId`) REFERENCES `chat_message` (`chatId`);
