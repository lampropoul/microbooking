package org.lampropoul.microbooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.lampropoul.microbooking.model.Booking;
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
public class BookingsControllerTests {

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
                createURLWithPort("/bookings/all"),
                HttpMethod.GET,
                null,
                Iterable.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
    }

    @Test
    @Order(2)
    void testGetById() {
        long id = 3L;
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings/" + id),
                HttpMethod.GET,
                null,
                Booking.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
        assert id == Objects.requireNonNull(response.getBody()).getId();
    }

    @Test
    @Order(3)
    void testGetByNonExistentId() {
        long id = 2L;
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings/" + id),
                HttpMethod.GET,
                null,
                Booking.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
        assert response.getBody() == null;
    }

    @Test
    @Order(4)
    void testCreateBooking() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("booking.json", Booking.class);
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings"),
                HttpMethod.POST,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    void testCreateExistentBooking() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("existentBooking.json", Booking.class);
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings"),
                HttpMethod.POST,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    void testUpdateBooking() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("existentBooking.json", Booking.class);
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings"),
                HttpMethod.PUT,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdateNonExistentHotel() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("booking.json", Booking.class);
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings"),
                HttpMethod.PUT,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    void testDeleteBooking() {
        long id = 3L;
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings/" + id),
                HttpMethod.DELETE,
                null,
                Booking.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(9)
    void testDeleteNonExistentBooking() {
        long id = 1L;
        ResponseEntity<Booking> response = restTemplate.exchange(
                createURLWithPort("/bookings/" + id),
                HttpMethod.DELETE,
                null,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
