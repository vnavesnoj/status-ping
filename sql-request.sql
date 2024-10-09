SELECT u2.nickname
FROM private_users p1
         JOIN private_users p2 ON p1.private_room_id = p2.private_room_id
         JOIN users u2 ON p2.user_id = u2.id
         JOIN users u1 ON p1.user_id = u1.id
         LEFT JOIN LATERAL (
    SELECT room_id, content, created_at
    FROM messages
    WHERE room_id = p2.private_room_id
    ORDER BY created_at DESC
    LIMIT 1
    ) m ON true
WHERE u1.nickname = ':1' AND u2.nickname != ':1';
