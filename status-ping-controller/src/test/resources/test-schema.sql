CREATE TABLE users
(
    id       INT PRIMARY KEY,
    nickname VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE user_user
(
    id       INT PRIMARY KEY,
    user1_id INT NOT NULL REFERENCES users,
    user2_id INT NOT NULL REFERENCES users,
    UNIQUE (user1_id, user2_id)
);