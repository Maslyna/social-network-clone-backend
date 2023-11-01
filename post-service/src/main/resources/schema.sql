DROP TABLE IF EXISTS t_hashtags CASCADE ;
DROP TABLE IF EXISTS t_posts CASCADE;
DROP TABLE IF EXISTS t_post_hashtags CASCADE;
DROP TABLE IF EXISTS t_posts_comments CASCADE;
DROP TABLE IF EXISTS t_posts_likes CASCADE;
DROP TABLE IF EXISTS t_post_likes CASCADE;
DROP TABLE IF EXISTS t_comment_likes CASCADE;
DROP TABLE IF EXISTS t_comments CASCADE;
DROP TABLE IF EXISTS t_comments_comments CASCADE;
DROP TABLE IF EXISTS t_comments_likes CASCADE;
DROP TABLE IF EXISTS t_photos CASCADE;
DROP TABLE IF EXISTS t_posts_photos CASCADE;

CREATE TABLE t_photos
(
    id          UUID   NOT NULL,
    user_id     BIGINT NOT NULL,
    content_url VARCHAR(500),
    CONSTRAINT pk_t_photos PRIMARY KEY (id)
);

CREATE TABLE t_hashtags
(
    hashtag_id UUID NOT NULL,
    text       VARCHAR(255),
    CONSTRAINT pk_t_hashtags PRIMARY KEY (hashtag_id)
);

CREATE TABLE t_posts
(
    post_id          UUID NOT NULL,
    dtype            VARCHAR(31),
    user_id          BIGINT,
    status           VARCHAR(20),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    title            VARCHAR(100),
    text             VARCHAR(500),
    original_post_id UUID,
    CONSTRAINT pk_t_posts PRIMARY KEY (post_id)
);


CREATE TABLE t_comments
(
    comment_id               UUID NOT NULL,
    user_id                  BIGINT,
    post_id                  UUID,
    subcomment_on_comment_id UUID,
    text                     VARCHAR(255),
    photo_id                 UUID,
    status                   VARCHAR(10),
    created_at               TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_t_comments PRIMARY KEY (comment_id)
);

CREATE TABLE t_post_likes
(
    like_id    UUID NOT NULL,
    user_id    BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    post_id    UUID,
    CONSTRAINT pk_t_post_likes PRIMARY KEY (like_id)
);


CREATE TABLE t_comment_likes
(
    like_id    UUID NOT NULL,
    user_id    BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    comment_id UUID,
    CONSTRAINT pk_t_comment_likes PRIMARY KEY (like_id)
);


CREATE TABLE t_comments_comments
(
    comment_comment_id  UUID NOT NULL,
    comments_comment_id UUID NOT NULL,
    CONSTRAINT pk_t_comments_comments PRIMARY KEY (comment_comment_id, comments_comment_id)
);

CREATE TABLE t_comments_likes
(
    comment_comment_id UUID NOT NULL,
    likes_like_id      UUID NOT NULL,
    CONSTRAINT pk_t_comments_likes PRIMARY KEY (comment_comment_id, likes_like_id)
);

CREATE TABLE t_post_hashtags
(
    hashtag_id UUID NOT NULL,
    post_id    UUID NOT NULL,
    CONSTRAINT pk_t_post_hashtags PRIMARY KEY (hashtag_id, post_id)
);

CREATE TABLE t_posts_comments
(
    post_post_id        UUID NOT NULL,
    comments_comment_id UUID NOT NULL,
    CONSTRAINT pk_t_posts_comments PRIMARY KEY (post_post_id, comments_comment_id)
);

CREATE TABLE t_posts_likes
(
    post_post_id  UUID NOT NULL,
    likes_like_id UUID NOT NULL,
    CONSTRAINT pk_t_posts_likes PRIMARY KEY (post_post_id, likes_like_id)
);

CREATE TABLE t_posts_photos
(
    post_post_id UUID NOT NULL,
    photos_id    UUID NOT NULL,
    CONSTRAINT pk_t_posts_photos PRIMARY KEY (post_post_id, photos_id)
);

ALTER TABLE t_posts_comments
    ADD CONSTRAINT uc_t_posts_comments_comments_comment UNIQUE (comments_comment_id);

ALTER TABLE t_posts_likes
    ADD CONSTRAINT uc_t_posts_likes_likes_like UNIQUE (likes_like_id);

ALTER TABLE t_posts_photos
    ADD CONSTRAINT uc_t_posts_photos_photos UNIQUE (photos_id);

ALTER TABLE t_posts
    ADD CONSTRAINT FK_T_POSTS_ON_ORIGINAL_POST FOREIGN KEY (original_post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_posts_comments
    ADD CONSTRAINT fk_tposcom_on_comment FOREIGN KEY (comments_comment_id) REFERENCES t_comments (comment_id);

ALTER TABLE t_posts_comments
    ADD CONSTRAINT fk_tposcom_on_post FOREIGN KEY (post_post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_post_hashtags
    ADD CONSTRAINT fk_tposhas_on_hashtag FOREIGN KEY (hashtag_id) REFERENCES t_hashtags (hashtag_id);

ALTER TABLE t_post_hashtags
    ADD CONSTRAINT fk_tposhas_on_post FOREIGN KEY (post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_posts_likes
    ADD CONSTRAINT fk_tposlik_on_post FOREIGN KEY (post_post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_posts_likes
    ADD CONSTRAINT fk_tposlik_on_post_like FOREIGN KEY (likes_like_id) REFERENCES t_post_likes (like_id);

ALTER TABLE t_posts_photos
    ADD CONSTRAINT fk_tpospho_on_photo FOREIGN KEY (photos_id) REFERENCES t_photos (id);

ALTER TABLE t_posts_photos
    ADD CONSTRAINT fk_tpospho_on_post FOREIGN KEY (post_post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_comments_comments
    ADD CONSTRAINT uc_t_comments_comments_comments_comment UNIQUE (comments_comment_id);

ALTER TABLE t_comments_likes
    ADD CONSTRAINT uc_t_comments_likes_likes_like UNIQUE (likes_like_id);

ALTER TABLE t_comment_likes
    ADD CONSTRAINT FK_T_COMMENT_LIKES_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES t_comments (comment_id);

ALTER TABLE t_post_likes
    ADD CONSTRAINT FK_T_POST_LIKES_ON_POST FOREIGN KEY (post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_comments
    ADD CONSTRAINT FK_T_COMMENTS_ON_PHOTO FOREIGN KEY (photo_id) REFERENCES t_photos (id);

ALTER TABLE t_comments
    ADD CONSTRAINT FK_T_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES t_posts (post_id);

ALTER TABLE t_comments
    ADD CONSTRAINT FK_T_COMMENTS_ON_SUBCOMMENT_ON_COMMENT FOREIGN KEY (subcomment_on_comment_id) REFERENCES t_comments (comment_id);

ALTER TABLE t_comments_comments
    ADD CONSTRAINT fk_tcomcom_on_comment_comment FOREIGN KEY (comment_comment_id) REFERENCES t_comments (comment_id);

ALTER TABLE t_comments_comments
    ADD CONSTRAINT fk_tcomcom_on_comments_comment FOREIGN KEY (comments_comment_id) REFERENCES t_comments (comment_id);

ALTER TABLE t_comments_likes
    ADD CONSTRAINT fk_tcomlik_on_comment FOREIGN KEY (comment_comment_id) REFERENCES t_comments (comment_id);

ALTER TABLE t_comments_likes
    ADD CONSTRAINT fk_tcomlik_on_comment_like FOREIGN KEY (likes_like_id) REFERENCES t_comment_likes (like_id);