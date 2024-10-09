INSERT INTO users(id, nickname)
VALUES (1, 'dummy1'),
       (2, 'dummy2'),
       (3, 'dummy3'),
       (4, 'dummy4'),
       (5, 'dummy5');

INSERT INTO user_user(id, user1_id, user2_id)
VALUES (1, 1, 2),
       (2, 1, 3),
       (3, 1, 4),
       (5, 2, 3),
       (6, 2, 4),
       (7, 3, 4)