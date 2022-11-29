
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `oasip_db` DEFAULT CHARACTER SET utf8 ;
USE `oasip_db` ;

CREATE TABLE IF NOT EXISTS `oasip_db`.`eventCategory` (
  `eventCategoryId` INT NOT NULL,
  `eventCategoryName` VARCHAR(100) NOT NULL,
  `eventCategoryDescription` VARCHAR(500) NULL,
  `eventDuration` INT NOT NULL,
  UNIQUE INDEX `eventCategoryName_UNIQUE` (`eventCategoryName` ASC) VISIBLE,
  PRIMARY KEY (`eventCategoryId`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `oasip_db`.`event` (
  `eventId` INT NOT NULL auto_increment,
  `bookingName` VARCHAR(100) NOT NULL,
  `bookingEmail` VARCHAR(100) NOT NULL,
  `eventCategoryId` INT NOT NULL,
  `eventStartTime` DATETIME(5) NOT NULL,
  `eventDuration` INT NOT NULL,
  `eventNote` VARCHAR(500) NULL,
   `fileName` VARCHAR(500) NULL,
  PRIMARY KEY (`eventId`),
  INDEX `fk_event_eventCategory_idx` (`eventCategoryId` ASC) VISIBLE,
  CONSTRAINT `fk_event_eventCategory`
    FOREIGN KEY (`eventCategoryId`)
    REFERENCES `oasip_db`.`eventCategory` (`eventCategoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- integrated 2-- 
create table users(
userId int not null auto_increment,
name varchar(100) not null unique,
email varchar(50) not null unique,
password varchar(100) not null ,
role enum('admin','lecturer','student') not null default 'student',
createdOn timestamp  not null default current_timestamp ,
updatedOn timestamp not null default current_timestamp on update current_timestamp,
primary key (userId)
);

-- about lecturure role
CREATE TABLE IF NOT EXISTS eventCategoryOwner (
  `id` INT NOT NULL auto_increment,
  `userId` INT NOT NULL,
  `eventCategoryId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_eventCategoryOwner_users_idx` (`userId` ASC) VISIBLE,
  INDEX `fk_eventCategoryOwner_eventCategories_idx` (`eventCategoryId` ASC) VISIBLE,
  CONSTRAINT `fk_eventCategoryOwner_users`
  FOREIGN KEY (`userId`)
  REFERENCES `oasip_db`.`users` (`userId`)  
  ON DELETE CASCADE
  ON UPDATE CASCADE
  -- ON DELETE NO ACTION
  -- ON UPDATE NO ACTION
  ,
  CONSTRAINT `fk_eventCategoryOwner_eventCategory`
  FOREIGN KEY (`eventCategoryId`)
  REFERENCES `oasip_db`.`eventCategory` (`eventCategoryId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- event category-base
insert into eventCategory (eventCategoryId ,eventCategoryName , eventCategoryDescription ,eventDuration)
values (1,'Project Management Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project | ให้นักศึกษาเตรียมเอกสารที่เกี่ยวขงเพื่อแสดงระหว่างขอคำปรึกษา',30);
insert into eventCategory (eventCategoryId ,eventCategoryName , eventCategoryDescription ,eventDuration)
values (2,'DevOps/Infra Clinic','Use this event category for DevOps/Infra clinic',20);
insert into eventCategory (eventCategoryId ,eventCategoryName , eventCategoryDescription ,eventDuration)
values (3,'Database Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย  database clinic ในวิชา INT221 integrated project |',15);
insert into eventCategory (eventCategoryId ,eventCategoryName , eventCategoryDescription ,eventDuration)
values (4,'Client-side Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย  client-side clinic ในวิชา INT221 integrated project |',30);
insert into eventCategory (eventCategoryId ,eventCategoryName , eventCategoryDescription ,eventDuration)
values (5,'Server-side Clinic',null,30);
-- ------------------------------

-- event-base
insert into event (eventId , bookingName , bookingEmail, eventStartTime, eventNote, eventDuration, eventCategoryId )
values (1,'Somchai Jaidee (OR-7)','somchai.jai@mail.kmutt.ac.th','2022-05-22 13:30:00',null,30,2);
insert into event (eventId , bookingName , bookingEmail, eventStartTime, eventNote, eventDuration, eventCategoryId )
values (2,'Somsri Rakdee (SJ-3)','somsri.rak@mail.kmutt.ac.th','2022-04-27 09:30:00','ขอปรึกษาปัญหาเพื่อนไม่ช่วยงาน',30,1);
insert into event (eventId , bookingName , bookingEmail, eventStartTime, eventNote, eventDuration, eventCategoryId )
values (3,'สมเกียรติ ขยันเรียน กลุ่ม TT-4','somkiat.kay@kmutt.ac.th','2022-05-23 16:30:00',null,15,3);
-- ------------------------------

-- user-with-authorization-base
insert into users value (1,"OASIP ADMIN","oasip.admin@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$sYXzbUOqBoHY1NfhJ8cjnw$H6+adWySiFPgcUogJK3hEhcF6Y4fusy7tcXYEL+f0cQ","admin",'2022-08-01 00:00:00+07:00','2022-08-01 00:00:00+07:00');
insert into users value (2,"Olarn Rojanapornpun","olarn.roj@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$Sx7y2jxKZSjpWUV4srd8eg$AMH09iFiPQgAZ00cAdN3Gucqfhx2kRo3tQbHeLSR0RE","lecturer",'2022-08-08 15:00:00+07:00','2022-08-08 15:00:00+07:00');
insert into users value (3,"Pichet Limvachiranan","pichet.limv@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$46EB43gQ46Z1/EmdqxtKNA$7m6cWGO2iDlFl/ETDYuYf+ArnSjRnsNwXLIP18DTYQY","lecturer",'2022-08-08 15:00:01+07:00','2022-08-08 15:00:01+07:00');
insert into users value (4,"Umaporn Supasitthimethee","umaporn.sup@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$1Z2UK1zC76FIQeLH54GVAQ$qfXcHF31LnuWpt37QAcWyNp8PdbOQ+jjaV1xWXixS0M","lecturer",'2022-08-08 15:00:02+07:00','2022-08-08 15:00:02+07:00');
insert into users value (5,"Siam Yamsaengsung","siam.yam@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$C4pPaNWKTnZQX2mPs14jlg$rQ5W5NYKqGOu1B4GkUWq8cFbcg2peFWGjpUMr9Nkm8g","lecturer",'2022-08-08 15:00:03+07:00','2022-08-08 15:00:03+07:00');
insert into users value (6,"Sunisa Sathapornvajana","sunisa.sat@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$29/ffaszvjvi3CZO45bSCg$kKpfq5WEswoqa/LfyIZzQaQ6AFdjhyiYjXRCfMiTnwg","lecturer",'2022-08-08 15:00:04+07:00','2022-08-08 15:00:04+07:00');
insert into users value (7,"Somchai Jaidee","somchai.jai@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$dmsOy7LPTjmooPu+P2oTZA$NZFTFd3f0K1Sp19aaUwyn3jgiy15yFcXhp8E4/1yXoI","student",'2022-08-08 16:00:00+07:00','2022-08-08 16:00:00+07:00');
insert into users value (8,"Komkrid Rakdee","komkrid.rak@mail.kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$8W61ZOC5RU7sJP5kKRbSqg$OLwZNPeMqxp+g0Vbn+odcA47XMClFN+IswTueVah7F0","student",'2022-08-08 16:00:00+07:00','2022-08-08 16:00:00+07:00');
insert into users value (9,"สมเกียรติ ขยันเรียน","somkiat.kay@kmutt.ac.th","$argon2id$v=19$m=4096,t=3,p=1$gBqgjspF45FcIKQEw8GmaQ$alrOCZ0YrDqOu8/aZiLDMGZo4vFkSEAXA0YoHhY0BDQ","student",'2022-08-16 09:00:00+07:00','2022-08-16 09:00:00+07:00');
insert into users(name,email,password,role) value ("admin","admin@gmail.com","$argon2id$v=19$m=16,t=2,p=1$o96rj2zPHrhw23uhdp3ElQ$lyQNNVpjVNeGkiyhV+s1LZ2csivQ/wsTTuPyneo","admin");
-- ------------------------------

-- event category-owner-base
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(1,2,1);
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(2,5,2);
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(3,6,3);
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(4,4,4);
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(5,3,5);
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(6,2,2);
INSERT INTO eventCategoryOwner(id,userid,eventCategoryId) VALUES(7,2,5);
-- ------------------------------
commit;


create user 'admin222'@'%' identified by 'admin222';
grant all privileges on *.* to 'admin222'@'%';
flush privileges;






