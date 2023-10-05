package net.maslyna.follower.model.dto.response;

import java.util.List;

public record FollowersResponse(
        List<Long> followers
) {
}
