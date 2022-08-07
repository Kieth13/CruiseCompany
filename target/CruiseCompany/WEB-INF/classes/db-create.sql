DROP database IF EXISTS cruise;

CREATE database cruise CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE cruise;

CREATE TABLE users (
	id INT PRIMARY KEY auto_increment,
	email VARCHAR(50) NOT NULL,
	password VARCHAR(30) NOT NULL,
    role VARCHAR(10) NOT NULL,
	name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	balance FLOAT);

CREATE TABLE ships (
	id INT PRIMARY KEY auto_increment,
    name VARCHAR(50) NOT NULL,
    passenger_amount INTEGER NOT NULL);

CREATE TABLE cruises (
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(30) NOT NULL,
    ship_id INT NOT NULL,
    places_reserved INTEGER NOT NULL,
    route_from VARCHAR(30) NOT NULL,
    route_to VARCHAR(30) NOT NULL,
    num_of_ports INTEGER NOT NULL,
    day_start DATE,
    day_end DATE,
    price FLOAT,
    FOREIGN KEY (ship_id) REFERENCES ships(id) ON DELETE CASCADE);

CREATE TABLE orders (
    id INT PRIMARY KEY auto_increment,
    cruise_id INT NOT NULL,
    user_id INT NOT NULL,
    num_of_passengers INT,
    total_price FLOAT,
    status VARCHAR(30) NOT NULL,
    FOREIGN KEY (cruise_id) REFERENCES cruises(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id ) REFERENCES users(id) ON DELETE CASCADE);

INSERT INTO users VALUES (DEFAULT, 'admin', 'admin', 'ADMIN', 'tony', 'stark', 0.0);
INSERT INTO users VALUES (DEFAULT, 'user2', 'pass2', 'CUSTOMER', 'loki', 'laufeyson', 1000);
INSERT INTO users VALUES (DEFAULT, 'user3', 'pass3', 'CUSTOMER', 'wanda', 'maximoff', 2000);

INSERT INTO ships VALUES (DEFAULT, 'aurora', 1800);
INSERT INTO ships VALUES (DEFAULT, 'Hebridean Princess', 150);

INSERT INTO cruises VALUES (DEFAULT, 'Caribbean & Bahamas', 1, 0, 'port canaveral', 'bimini islands', 3, '2022-06-24', '2022-06-30', 400);
INSERT INTO cruises VALUES (DEFAULT, 'Hebridean Princess', 2, 0, 'miami', 'harvest caye', 3, '2022-07-15', '2022-07-21', 1200);
INSERT INTO cruises VALUES (DEFAULT, 'Caribbean & Bahamas', 1, 0, 'port canaveral', 'bimini islands', 3, '2022-07-21', '2022-07-25', 400);

INSERT INTO orders VALUES (DEFAULT, 1, 2, 1, 400, 'COMPLETED');
INSERT INTO orders VALUES (DEFAULT, 1, 3, 1, 500, 'COMPLETED');
INSERT INTO orders VALUES (DEFAULT, 2, 2, 1, 1200, 'RESERVED');
