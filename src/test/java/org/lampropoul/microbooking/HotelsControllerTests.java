package org.lampropoul.microbooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.lampropoul.microbooking.model.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelsControllerTests {

    final TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ResourceLoader resourceLoader;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private <T> HttpEntity<T> getRequestEntity(String resourceFilename, Class<T> classType) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceFilename);
        T entity = objectMapper.readValue(resource.getFile(), classType);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediaTypes);
        return new HttpEntity<>(entity, httpHeaders);
    }

    @Test
    @Order(1)
    void testGetAll() {
        ResponseEntity<Iterable> response = restTemplate.exchange(
                createURLWithPort("/hotels/all"),
                HttpMethod.GET,
                null,
                Iterable.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
    }

    @Test
    @Order(2)
    void testGetById() {
        long id = 1L;
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels/" + id),
                HttpMethod.GET,
                null,
                Hotel.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
        assert id == Objects.requireNonNull(response.getBody()).getId();
    }

    @Test
    @Order(3)
    void testGetByNonExistentId() {
        long id = 42L;
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels/" + id),
                HttpMethod.GET,
                null,
                Hotel.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
        assert response.getBody() == null;
    }

    @Test
    @Order(4)
    void testCreateHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("hotel.json", Hotel.class);
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels"),
                HttpMethod.POST,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    void testCreateExistentHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("existentHotel.json", Hotel.class);
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels"),
                HttpMethod.POST,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    void testUpdateHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("existentHotel.json", Hotel.class);
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels"),
                HttpMethod.PUT,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdateNonExistentHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("hotel.json", Hotel.class);
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels"),
                HttpMethod.PUT,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    void testDeleteHotel() {
        long id = 1L;
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels/" + id),
                HttpMethod.DELETE,
                null,
                Hotel.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(9)
    void testDeleteNonExistentHotel() {
        long id = 33L;
        ResponseEntity<Hotel> response = restTemplate.exchange(
                createURLWithPort("/hotels/" + id),
                HttpMethod.DELETE,
                null,
                Hotel.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
