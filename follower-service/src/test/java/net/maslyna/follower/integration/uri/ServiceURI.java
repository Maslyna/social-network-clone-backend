package net.maslyna.follower.integration.uri;

public class ServiceURI {
    public final static String FOLLOW = "/api/v1/user/%s/followers";
    public final static String UNFOLLOW = "/api/v1/user/%s/followers";
    public final static String IS_FOLLOWED = "/api/v1/user/%s/is-followed";
    public final static String IS_SUBSCRIBED = "/api/v1/user/%s/is-subscribed";
    public final static String GET_PRIVATE_FOLLOWERS = "/api/v1/user/followers";
    public final static String GET_FOLLOWERS = "/api/v1/user/%s/followers";
    public final static String GET_PRIVATE_SUBSCRIPTIONS = "/api/v1/user/subscriptions";
    public final static String GET_SUBSCRIPTIONS = "/api/v1/user/%s/subscriptions";
}
