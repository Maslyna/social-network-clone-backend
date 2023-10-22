INSERT INTO t_posts(post_id, dtype, user_id, status, created_at, title, text)
VALUES ('e72861b1-c99d-47cc-9837-4421f5d166d5', 'Post', 2, 'PUBLISHED', '2023-10-20 17:10:34.144975',
        'test post title with 20 characters', 'test post text');
INSERT INTO t_comments(comment_id, user_id, post_id, subcomment_on_comment_id, text, status, created_at)
VALUES ('a88660db-8acb-4ca7-a8c5-27d2946a0316', 2, 'e72861b1-c99d-47cc-9837-4421f5d166d5', NULL, 'default comment text',
        'NORMAL', '2023-10-20 17:10:34.144975');