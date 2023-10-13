package net.maslyna.notification.service.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
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
}
