SELECT p1.private_room_id,
       u.username,
       u.nickname,
       COALESCE(u.avatar, '') AS avatar,
       COALESCE(m.content, '') AS content,
       COALESCE(m.created_at::text, '') AS created_at
FROM private_users p1
         JOIN private_users p2 ON p1.private_room_id = p2.private_room_id
         JOIN users u ON p2.user_id = u.id
         LEFT JOIN LATERAL (
    SELECT room_id, content, created_at
    FROM messages
    WHERE room_id = p2.private_room_id
    ORDER BY created_at DESC
    LIMIT 1
    ) m ON true
WHERE p1.user_id = '3c760f4f-f2c0-4914-856b-9e0d608127fa' AND p2.user_id != '3c760f4f-f2c0-4914-856b-9e0d608127fa';
