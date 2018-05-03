package io.block16.assetapi.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.block16.response.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FeignErrorDecoder implements ErrorDecoder {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper objectMapper;

    public FeignErrorDecoder() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        RuntimeException exception = new RuntimeException("Failed to decode response from upstream: " + methodKey);
        try {
            GenericResponse<Object> generic =
                    this.objectMapper.readValue(response.body().asReader(), GenericResponse.class);
            if(response.status() >= 400 && response.status() < 500) {
                LOGGER.error("Made bad request to upstream, methodKey: " + methodKey + ", message: " + generic.getMessage() + ", body: " + generic.getData());
                exception = new InternalServerException("Made bad request to upstream. Tell administrator to check logs.");
            } else if (response.status() >= 500) {
                LOGGER.warn("Upstream server error, methodKey: " + methodKey + ", message: " + generic.getMessage() + ", body: " + generic.getData());
                exception = new UpstreamException("Upstream had an internal server error, methodKey: " + methodKey);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not decode error message from feign client: " + ex.getMessage());
        }
        return exception;
    }
}
