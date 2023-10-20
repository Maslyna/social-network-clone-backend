package net.maslyna.post.integration.uri;

public class CommentURI {
    public static final String CREATE_COMMENT = "/api/v1/post/%s/comments";
    public static final String GET_COMMENTS = "/api/v1/post/%s/comments";
    public static final String CREATE_SUB_COMMENT = "/api/v1/post/%s/comments/%s";
    public static final String EDIT_COMMENT = "/api/v1/post/%s/comments/%s";
    public static final String DELETE_COMMENT = "/api/v1/post/%s/comments/%s";
}
