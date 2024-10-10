SELECT u2.nickname
FROM user_user uu
         JOIN users u1 on uu.user1_id = u1.id
         JOIN users u2 on uu.user2_id = u2.id
WHERE u1.nickname = :nickname