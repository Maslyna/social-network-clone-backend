package net.maslyna.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.io.IOException;
import java.util.Map;


@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorResponse = super.getErrorAttributes(request, options);
        Throwable error = super.getError(request);

        try {
            return parseErrorMessage(error.getMessage());
        } catch (Throwable e) {
            return errorResponse;
        }

    }


    public Map<String, Object> parseErrorMessage(String errorMessage) throws IOException {
        String jsonPart = errorMessage.substring(errorMessage.indexOf("{"), errorMessage.lastIndexOf("}") + 1);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parseResult = objectMapper.readValue(jsonPart, Map.class);
        log.info("parseResult = {}", parseResult);
        return parseResult;
    }
}