CREATE SCHEMA IF NOT EXISTS `oasip_db` DEFAULT CHARACTER SET utf8 ;
USE `oasip_db` ;

CREATE TABLE IF NOT EXISTS `oasip_db`.`eventCategories` (
  `categoryId` INT NOT NULL auto_increment,
  `eventCategoryName` VARCHAR(100) NOT NULL unique,
  `eventCategoryDescription` VARCHAR(500)  NULL ,
  `eventDuration` INT NOT NULL,
  PRIMARY KEY (`categoryId`),
  UNIQUE INDEX `eventCategoryName_UNIQUE` (`eventCategoryName` ASC) VISIBLE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `oasip_db`.`events` (
  `eventId` INT NOT NULL auto_increment,
  `bookingName` VARCHAR(100) NOT NULL,
  `bookingEmail` VARCHAR(100) NOT NULL,
  `eventCategory` INT NOT NULL,
  `eventStartTime` DATETIME NOT NULL ,
  `eventDuration` INT  ,
  `eventNotes` VARCHAR(500) NULL,
  PRIMARY KEY (`eventId`),
  INDEX `fk_Events_EventCategories_idx` (`eventCategory` ASC) VISIBLE,
  CONSTRAINT `fk_Events_EventCategories`
    FOREIGN KEY (`eventCategory`)
    REFERENCES `oasip_db`.`eventCategories` (`categoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)

ENGINE = InnoDB;

use oasip_db;

-- insert into eventCategory (eventCategoryName , eventCategoryDescription ,eventDuration)
-- values ('Project Management Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา', 30);
-- insert into eventCategories (eventCategoryName , eventCategoryDescription ,eventDuration)
-- values ('DevOps/Infra Clinic','Use this event category for DevOps/Infra clinic.',20);
-- insert into eventCategories (eventCategoryName , eventCategoryDescription ,eventDuration)
-- values ('Database Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย  database clinic ในวิชา INT221 integrated project I',15);
-- insert into eventCategories (eventCategoryName , eventCategoryDescription ,eventDuration)
-- values ('Client-side Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย  client-side clinic ในวิชา INT221 integrated project I',30);
-- insert into eventCategories (eventCategoryName ,eventDuration)
-- values ('Server-side Clinic',30);
-- insert into eventCategory 
-- values (1,'ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา','Project Management Clinic', 30);

-- insert into events (bookingName , bookingEmail,eventCategory,eventStartTime, eventDuration,  eventNotes)
-- values ('Somchai Jaidee (OR-7)','somchai.jai@mail.kmutt.ac.th',2,'2022-05-23 13:30:00',30,null);

-- insert into events (bookingName , bookingEmail,eventCategory,eventStartTime, eventDuration,  eventNotes)
-- values ('Somsri Rakdee (SJ-3)','somsri.rak@mail.kmutt.ac.th',1,'2022-04-27 09:30:00',30, 'ขอปรึกษาปัญหาเพื่อนไม่ช่วยงาน');

-- insert into events (bookingName , bookingEmail,eventCategory,eventStartTime, eventDuration, eventNotes)
-- values ('สมเกียรติ ขยันเรียน กลุ่ม TT-4','somkiat.kay@kmutt.ac.th',3,'2022-05-23 16:30:00',15,null);

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

insert into users (name,email,password,role) value('admin','admin','adminadmin','admin');

create user 'admin222'@'%' identified by 'admin222';
grant all privileges on *.* to 'admin222'@'%';
flush privileges;
