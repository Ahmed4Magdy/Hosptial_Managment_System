create table patient(


 id  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 full_name VARCHAR(100) NOT NULL,
 email VARCHAR(100) UNIQUE NOT NULL,
 phone_number VARCHAR(20),
 gender VARCHAR(10),
 birth_date DATE,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP



);