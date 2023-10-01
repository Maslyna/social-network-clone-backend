INSERT INTO t_posts(created_at, user_id, post_id, status, title, text)
VALUES ('2023-10-01 18:57:57.634359+00', 1, 'a2e04c68-942c-41f7-852b-45b1a50dec18', 'PUBLISHED', 'test post',
        'test post text');

INSERT INTO t_hashtags(hashtag_id, text)
VALUES ('fd48ece6-ef9d-436c-88e8-fd12130ff8b2', 'tag1'),
       ('5b16c190-b64a-4fd3-8c07-8be936fab9fa', 'tag2'),
       ('edf94f9d-a9ca-46ae-b6b5-b20efde9f28c', 'tag3');

INSERT INTO t_post_hashtags(hashtag_id, post_id)
VALUES ('5b16c190-b64a-4fd3-8c07-8be936fab9fa', 'a2e04c68-942c-41f7-852b-45b1a50dec18'),
       ('fd48ece6-ef9d-436c-88e8-fd12130ff8b2', 'a2e04c68-942c-41f7-852b-45b1a50dec18'),
       ('edf94f9d-a9ca-46ae-b6b5-b20efde9f28c', 'a2e04c68-942c-41f7-852b-45b1a50dec18');

INSERT INTO t_comments(created_at, user_id, status, comment_id, post_id, subcomment_on_comment_id, text)
VALUES ('2023-10-01 18:58:12.960408+00', 2, 'NORMAL', '8b58342e-2ce4-4e41-9fe3-6f10b5f4e3f1',
        'a2e04c68-942c-41f7-852b-45b1a50dec18', NULL, 'new comment'),
       ('2023-10-01 18:58:27.405688+00', 2, 'NORMAL', '41ae8e1b-1e5c-4e55-93c5-0121f5131a2d',
        'a2e04c68-942c-41f7-852b-45b1a50dec18', '8b58342e-2ce4-4e41-9fe3-6f10b5f4e3f1', 'new comment');

INSERT INTO t_posts_comments(comments_comment_id, post_post_id)
VALUES ('8b58342e-2ce4-4e41-9fe3-6f10b5f4e3f1',	'a2e04c68-942c-41f7-852b-45b1a50dec18');

INSERT INTO t_likes(created_at, user_id, comment_id, like_id, post_id)
VALUES ('2023-10-01 18:58:44.298633+00', 2, '8b58342e-2ce4-4e41-9fe3-6f10b5f4e3f1', '3ad9c780-73a3-4026-9b23-4f128d95f59f', NULL);

INSERT INTO t_comments_comments (comment_comment_id, comments_comment_id)