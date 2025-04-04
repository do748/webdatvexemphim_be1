-- Tạo cơ sở dữ liệu cinema
CREATE DATABASE IF NOT EXISTS cinema;
USE cinema;

-- Tạo bảng movie trước vì có khóa ngoại liên quan đến nó
CREATE TABLE IF NOT EXISTS `movie` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modified_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `actor` VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `director` VARCHAR(255) DEFAULT NULL,
  `duration` TIME NOT NULL,
  `genre` ENUM('ACTION','COMEDY','DRAMA','HORROR','ROMANCE','SCI_FI') DEFAULT NULL,
  `image_movie` VARCHAR(255) NOT NULL,
  `language` ENUM('ENGLISH','INDIA','JAPANESE','VIETNAMESE') DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `rating` FLOAT DEFAULT NULL,
  `start_date` DATE NOT NULL,
  `status` ENUM('CLOSE','COMING_SOON','SHOWING','SPECIAL') DEFAULT NULL,
  `trailer` VARCHAR(255) NOT NULL,
  `viewing_age` ENUM('CHILD','G','PG','PG13','R','T15','T18') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tạo bảng account
CREATE TABLE IF NOT EXISTS `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modified_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `address` VARCHAR(255) DEFAULT NULL,
  `birth_date` DATE DEFAULT NULL,
  `city` VARCHAR(255) DEFAULT NULL,
  `district` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `fullname` VARCHAR(255) DEFAULT NULL,
  `gender` ENUM('FEMALE','MALE') DEFAULT NULL,
  `passport` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `role` ENUM('ADMIN','MANAGER','USER') NOT NULL,
  `status` ENUM('ACTIVE','BLOCK','INACTIVE','PENDING') NOT NULL DEFAULT 'PENDING',
  `username` VARCHAR(255) NOT NULL UNIQUE,
  `verification_code` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tạo bảng banner
CREATE TABLE IF NOT EXISTS `banner` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modified_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` VARCHAR(255) DEFAULT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  `status` ENUM('ACTIVE','INACTIVE') DEFAULT NULL,
  `title` VARCHAR(255) DEFAULT NULL,
  `movie_id` INT DEFAULT NULL,
  `is_active` BOOLEAN DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_banner_movie` (`movie_id`),
  CONSTRAINT `FK_banner_movie` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `room` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `cinema_id` INT NOT NULL,
    `status` ENUM('AVAILABLE', 'MAINTENANCE', 'BOOKED') NOT NULL,
    `screen_type` ENUM('IMAX', 'NORMAL') NOT NULL
);

CREATE TABLE IF NOT EXISTS `contact` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `created_date` DATETIME(6),
    `modified_date` DATETIME(6),
    `city` ENUM('BAC_NINH', 'BIEN_HOA', 'CAN_THO', 'CA_MAU', 'DA_NANG', 'HAI_PHONG', 'HANOI', 'HUE', 'NHA_TRANG', 'TPHCM', 'VUNG_TAU'),
    `status` ENUM('FAILED', 'PENDING', 'SUCCESS'),
    `details` VARCHAR(255),
    `email` VARCHAR(255),
    `fullname` VARCHAR(255),
    `phone_number` VARCHAR(255),
    `service_contact` ENUM('ADS_THEATERS', 'BUY_GROUP_TICKET', 'E_CODE', 'RENT_EVENTS'),
    `cinema_id` INT
);


CREATE TABLE IF NOT EXISTS `seat` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(10) NOT NULL,
    `seat_type` ENUM('SINGLE', 'VIP', 'DOUBLE') NOT NULL,
    `price` DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS `seat_room` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `room_id` INT NOT NULL,
    `type_seat` ENUM('SINGLE', 'VIP', 'DOUBLE') NOT NULL,
    `status` ENUM('AVAILABLE', 'OCCUPIED') NOT NULL,
    FOREIGN KEY (`room_id`) REFERENCES `room`(`id`) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `showtime` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `movie_id` INT NOT NULL,
    `room_id` INT NOT NULL,
    `cinema_id` INT NOT NULL,
    `show_date` DATE NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `list_start_time` (
    `id` SERIAL PRIMARY KEY,
    `showtime_id` INT NOT NULL,
    `start_time` TIME NOT NULL,
    FOREIGN KEY (showtime_id) REFERENCES showtime(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `ticket` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `account_id` INT NOT NULL,
    `seat_room_id` INT NOT NULL,
    `show_time_id` INT NOT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `status` ENUM('PAID', 'UNPAID', 'CANCELLED') NOT NULL,
    FOREIGN KEY (`show_time_id`) REFERENCES `showtime`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `more_service` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `image` TEXT NOT NULL,
    `description` TEXT NOT NULL,
    `price` DECIMAL(10,2) NOT NULL,
    `status` ENUM('ACTIVE', 'INACTIVE') NOT NULL
);

CREATE TABLE IF NOT EXISTS `payment` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `method` ENUM('E_WALLET', 'BANKING') NOT NULL,
    `address_transfer` TEXT NOT NULL,
    `qr_code` TEXT NOT NULL,
    `status` ENUM('ACTIVE', 'INACTIVE') NOT NULL
);

CREATE TABLE IF NOT EXISTS `booking` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `account_id` INT NOT NULL,
    `ticket_id` INT NOT NULL,
    `more_service_id` INT DEFAULT NULL,
    `voucher_id` INT DEFAULT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `status` ENUM('SUCCESS', 'PENDING', 'CANCELLED') NOT NULL,
    FOREIGN KEY (`ticket_id`) REFERENCES `ticket`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`more_service_id`) REFERENCES `more_service`(`id`) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS `receipt` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `type` ENUM('INCOME', 'SPENDING') NOT NULL,
    `booking_id` INT NOT NULL,
    `account_id` INT NOT NULL,
    `reason` VARCHAR(255) NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `status` ENUM('PROCESSED', 'UNPROCESSED') NOT NULL
);



-- Tạo bảng cinema
CREATE TABLE IF NOT EXISTS `cinema` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modified_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `location` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL UNIQUE,
  `status` ENUM('BUILDING','CLOSED','OPEN') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tạo bảng cinema_images
CREATE TABLE IF NOT EXISTS `cinema_images` (
  `cinema_id` INT NOT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,
  KEY `FK_cinema_images` (`cinema_id`),
  CONSTRAINT `FK_cinema_images` FOREIGN KEY (`cinema_id`) REFERENCES `cinema` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tạo bảng voucher
CREATE TABLE IF NOT EXISTS `voucher` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modified_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` VARCHAR(255) DEFAULT NULL,
  `discount` DOUBLE NOT NULL,
  `expiry` DATE NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `quantity` INT DEFAULT NULL,
  `status` ENUM('EFFECTIVE','EXPIRED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

