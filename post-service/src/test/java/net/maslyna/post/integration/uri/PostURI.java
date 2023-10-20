package net.maslyna.post.integration.uri;

public class PostURI {
    public static final String CREATE_POST = "/api/v1/post/create";
    public static final String CREATE_REPOST = "/api/v1/post/%s/repost";
    public static final String EDIT_POST = "/api/v1/post/%s";
    public static final String GET_ALL_PUBLISHED_POSTS = "/api/v1/post";
    public static final String GET_ALL_PERSONS_POSTS = "/api/v1/post/user/%s";
    public static final String GET_POST = "/api/v1/post/%s";
    public static final String DELETE_POST = "/api/v1/post/%s";
}
