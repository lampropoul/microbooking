package org.lampropoul.microbooking;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.lampropoul.microbooking.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingsControllerTests {

    final TestRestTemplate restTemplate = new TestRestTemplate();
    final TestHelper helper = new TestHelper();
    @LocalServerPort
    private int port;
    @Autowired
    private ResourceLoader resourceLoader;

    private HttpEntity<Booking> getRequestEntity(String resourceFilename) throws IOException {
        return helper.getRequestEntity(resourceFilename, Booking.class, resourceLoader);
    }

    @Test
    @Order(0)
    void testGetAllBookingsForAHotel() {
        ResponseEntity<List> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings/byHotelName/Mariott"),
                HttpMethod.GET,
                null,
                List.class);
        List<LinkedHashMap<String, String>> bookings = (List<LinkedHashMap<String, String>>) response.getBody();
        assert bookings != null;
        LinkedHashMap<String, String> booking = bookings.get(0);
        assert booking.get("id") != null;
    }

    @Test
    @Order(1)
    void testGetAll() {
        ResponseEntity<Iterable> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings/all"),
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
                helper.createURLWithPort(port, "/bookings/" + id),
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
                helper.createURLWithPort(port, "/bookings/" + id),
                HttpMethod.GET,
                null,
                Booking.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
        assert response.getBody() == null;
    }

    @Test
    @Order(4)
    void testCreateBooking() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("newBooking.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.POST,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    void testCreateExistentBooking() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("existentBooking.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.POST,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    void testUpdateBooking() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("existentBooking.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.PUT,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdateNonExistentHotel() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("newBooking.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.PUT,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    void testDeleteBooking() {
        long id = 4L;
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings/" + id),
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
                helper.createURLWithPort(port, "/bookings/" + id),
                HttpMethod.DELETE,
                null,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(10)
    void testUpdateBookingWithNewHotel() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("existentBookingWithNewHotel.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.PUT,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(11)
    void testCreateBookingWithNewHotel() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("newBookingWithNewHotel.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.POST,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(12)
    void testCreateBookingWithNullHotel() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("newBookingWithNullHotel.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.POST,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(13)
    void testUpdateBookingWithNullHotel() throws IOException {
        HttpEntity<Booking> bookingHttpEntity = getRequestEntity("existentBookingWithNullHotel.json");
        ResponseEntity<Booking> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/bookings"),
                HttpMethod.PUT,
                bookingHttpEntity,
                Booking.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
