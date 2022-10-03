
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

insert into event (eventId , bookingName , bookingEmail, eventStartTime, eventNote, eventDuration, eventCategoryId )
values (1,'Somchai Jaidee (OR-7)','somchai.jai@mail.kmutt.ac.th','2022-05-22 13:30:00',null,30,2);

insert into event (eventId , bookingName , bookingEmail, eventStartTime, eventNote, eventDuration, eventCategoryId )
values (2,'Somsri Rakdee (SJ-3)','somsri.rak@mail.kmutt.ac.th','2022-04-27 09:30:00','ขอปรึกษาปัญหาเพื่อนไม่ช่วยงาน',30,1);

insert into event (eventId , bookingName , bookingEmail, eventStartTime, eventNote, eventDuration, eventCategoryId )
values (3,'สมเกียรติ ขยันเรียน กลุ่ม TT-4','somkiat.kay@kmutt.ac.th','2022-05-23 16:30:00',null,15,3);


-- integrated 2-- 
create table users(
userId int not null auto_increment,
name varchar(100) not null unique,
email varchar(50) not null unique,
password varchar(90) not null ,
role enum('admin','lecturer','student') not null default 'student',
createdOn timestamp  not null default current_timestamp ,
updatedOn timestamp not null default current_timestamp on update current_timestamp,
primary key (userId)
);

insert into users(name,email,password,role,createdOn,updatedOn) value('OASIP ADMIN','oasip.admin@kmutt.ac.th','oasipadmin','admin','2022-08-01 00:00:00+07:00','2022-08-01 00:00:00+07:00');
insert into users(name,email,password,role,createdOn,updatedOn) value('Somchai Jaidee','somchai.jai@kmutt.ac.th','somchaijai','lecturer','2022-08-08 15:00:00+07:00','2022-08-08 15:00:00+07:00');
insert into users(name,email,password,role,createdOn,updatedOn) value('Komkrid Rakdee','komkrid.rak@mail.kmutt.ac.th','komkridrak','student','2022-08-08 15:00:01+07:00','2022-08-08 15:00:01+07:00');
insert into users(name,email,password,role,createdOn,updatedOn) value('สมเกียรติ ขยันเรียน','somkiat.kay@kmutt.ac.th','somkiat.kay','student','2022-08-16 09:00:00+07:00','2022-08-16 09:00:00+07:00');
insert into users(name,email,password,role) value ("admin","admin@gmail.com","$argon2id$v=19$m=16,t=2,p=1$o96rj2zPHrhw23uhdp3ElQ$lyQNNVpjVNeGkiyhV+s1LZ2csivQ/wsTTuPyneo","admin");
commit;

create user 'admin222'@'%' identified by 'admin222';
grant all privileges on *.* to 'admin222'@'%';
flush privileges;

use oasip_db;
select * from events;