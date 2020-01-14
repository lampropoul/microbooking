package org.lampropoul.microbooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final ObjectMapper objectMapper = new ObjectMapper();

    String createURLWithPort(int port, String uri) {
        return "http://localhost:" + port + uri;
    }

    <T> HttpEntity<T> getRequestEntity(
            String resourceFilename,
            Class<T> classType,
            ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceFilename);
        T entity = objectMapper.readValue(resource.getFile(), classType);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediaTypes);
        return new HttpEntity<>(entity, httpHeaders);
    }
}
