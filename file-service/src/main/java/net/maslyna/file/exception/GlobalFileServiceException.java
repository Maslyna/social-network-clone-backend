package net.maslyna.file.exception;

import lombok.Getter;
import net.maslyna.common.exception.AbstractServiceException;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
public class GlobalFileServiceException extends AbstractServiceException {

    public GlobalFileServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalFileServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalFileServiceException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
