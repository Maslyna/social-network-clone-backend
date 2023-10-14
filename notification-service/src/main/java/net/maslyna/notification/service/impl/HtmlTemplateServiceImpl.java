package net.maslyna.notification.service.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.CommentLikedEvent;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.common.kafka.dto.PostLikedEvent;
import net.maslyna.notification.model.HtmlTemplate;
import net.maslyna.notification.service.HtmlTemplateService;
import net.maslyna.notification.service.IOService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HtmlTemplateServiceImpl implements HtmlTemplateService {
    private final IOService ioService;

    @Override
    public String getHtmlTemplate(HtmlTemplate template) throws IOException {
        return ioService.getFileAsString(template.getPath());
    }

    @Override
    public String getHtmlTemplate(HtmlTemplate template, Object... args) throws IOException {
        if (template == null)
            return null;
        return ioService.getFileAsString(template.getPath())
                .formatted(args);
    }

    @Override
    public String getHtmlTemplate(PostCreatedEvent event) throws IOException {
        return getHtmlTemplate(
                HtmlTemplate.POST_CREATED,
                event.userId(),
                event.post(),
                event.rePost(),
                event.title(),
                event.createdAt()
        );
    }

    @Override
    public String getHtmlTemplate(PostLikedEvent event) throws IOException {
        return getHtmlTemplate(
                HtmlTemplate.POST_LIKED,
                event.postOwnerId(),
                event.postId(),
                event.userId()
        );
    }

    @Override
    public String getHtmlTemplate(CommentLikedEvent event) throws IOException {
        return getHtmlTemplate(
                HtmlTemplate.COMMENT_LIKED,
                event.postId(),
                event.commentId(),
                event.userId()
        );
    }
}
