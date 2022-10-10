CREATE TABLE users (
id SERIAL PRIMARY key,
first_name VARCHAR(50),
last_name VARCHAR(50),
username VARCHAR(50),
password VARCHAR(50)
);

INSERT INTO users (first_name, last_name, username, password) VALUES
('MATT', 'FRIED', 'admin', 'admin'),
('MIKE', 'SMYLY', 'admin', 'admin'),
('BRIAN', 'FREEMAN', 'admin', 'admin');

CREATE TABLE tasks (
id SERIAL PRIMARY key,
title VARCHAR(50),
created_at TIMESTAMP,
user_id Integer
);