CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME
    );

CREATE TABLE IF NOT EXISTS book (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    publisher VARCHAR(100),
    isbn VARCHAR(30) UNIQUE,
    stock INT NOT NULL,
    marker INT NOT NULL,
    description TEXT,
    create_time DATETIME NOT NULL,
    update_time DATETIME
    );

CREATE TABLE IF NOT EXISTS borrow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrow_time DATETIME NOT NULL,
    return_time DATETIME,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS subscribe (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    subscribe_time DATETIME NOT NULL
);