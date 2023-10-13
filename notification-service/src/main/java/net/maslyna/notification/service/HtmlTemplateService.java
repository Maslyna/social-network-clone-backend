package net.maslyna.notification.service;

import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.notification.model.HtmlTemplate;

import java.io.IOException;

public interface HtmlTemplateService {
    String getHtmlTemplate(HtmlTemplate template) throws IOException;
    String getHtmlTemplate(HtmlTemplate template, Object... args) throws IOException;

    String getHtmlTemplate(PostCreatedEvent event) throws  IOException;
}
