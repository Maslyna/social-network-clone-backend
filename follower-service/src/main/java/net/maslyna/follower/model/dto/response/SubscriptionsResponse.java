package net.maslyna.follower.model.dto.response;

import java.util.List;

public record SubscriptionsResponse(
        List<Long> subscriptions
) {
}
