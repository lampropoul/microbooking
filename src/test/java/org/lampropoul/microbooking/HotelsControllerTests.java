package org.lampropoul.microbooking;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.lampropoul.microbooking.model.Hotel;
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
public class HotelsControllerTests {

    final TestRestTemplate restTemplate = new TestRestTemplate();
    final TestHelper helper = new TestHelper();
    @LocalServerPort
    private int port;
    @Autowired
    private ResourceLoader resourceLoader;

    private HttpEntity<Hotel> getRequestEntity(String resourceFilename) throws IOException {
        return helper.getRequestEntity(resourceFilename, Hotel.class, resourceLoader);
    }

    @Test
    @Order(0)
    void testGetAllHotelThatASurnameHasBookingsFor() {
        ResponseEntity<List> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels/withBookingsFor/Lambropoulos"),
                HttpMethod.GET,
                null,
                List.class);
        List<LinkedHashMap<String, String>> hotels = (List<LinkedHashMap<String, String>>) response.getBody();
        assert hotels != null;
        LinkedHashMap<String, String> hotel = hotels.get(0);
        assert hotel.get("id") != null;
    }

    @Test
    @Order(1)
    void testGetAll() {
        ResponseEntity<Iterable> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels/all"),
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
                helper.createURLWithPort(port, "/hotels/" + id),
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
                helper.createURLWithPort(port, "/hotels/" + id),
                HttpMethod.GET,
                null,
                Hotel.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
        assert response.getBody() == null;
    }

    @Test
    @Order(4)
    void testCreateHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("newHotel.json");
        ResponseEntity<Hotel> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels"),
                HttpMethod.POST,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    void testCreateExistentHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("existentHotel.json");
        ResponseEntity<Hotel> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels"),
                HttpMethod.POST,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    void testUpdateHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("existentHotel.json");
        ResponseEntity<Hotel> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels"),
                HttpMethod.PUT,
                hotelHttpEntity,
                Hotel.class);
        assertSame(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testUpdateNonExistentHotel() throws IOException {
        HttpEntity<Hotel> hotelHttpEntity = getRequestEntity("newHotel.json");
        ResponseEntity<Hotel> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels"),
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
                helper.createURLWithPort(port, "/hotels/" + id),
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
                helper.createURLWithPort(port, "/hotels/" + id),
                HttpMethod.DELETE,
                null,
                Hotel.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(11)
    void testPriceAmountForHotel() {
        long id = 2L;
        ResponseEntity<Float> response = restTemplate.exchange(
                helper.createURLWithPort(port, "/hotels/" + id + "/priceAmounts"),
                HttpMethod.GET,
                null,
                Float.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
        assert 3979.22f == response.getBody();
    }

}
