package net.maslyna.post.integration.uri;

public class LikeURI {
    public static final String LIKE_POST = "/api/v1/post/%s/like";
    public static final String LIKE_COMMENT = "/api/v1/post/comments/%s/like";
    public static final String GET_COMMENTS_LIKES = "/api/v1/post/comments/%s/like";
    public static final String GET_POSTS_LIKES = "/api/v1/post/%s/like";
    public static final String DELETE_LIKE_ON_POST = "/api/v1/post/%s/like";
    public static final String DELETE_LIKE_ON_COMMENT = "/api/v1/post/comments/%s/like";
}
