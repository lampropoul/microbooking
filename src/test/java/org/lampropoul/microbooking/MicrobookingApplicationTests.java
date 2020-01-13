package org.lampropoul.microbooking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MicrobookingApplicationTests {

    final TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    void testGetAllHotelThatASurnameHasBookingsFor() {
        ResponseEntity<List> response = restTemplate.exchange(createURLWithPort("/hotels/bookingsFor/Lambropoulos"), HttpMethod.GET, null, List.class);
        List<LinkedHashMap<String, String>> hotels = (List<LinkedHashMap<String, String>>) response.getBody();
        assert hotels != null;
        LinkedHashMap<String, String> hotel = hotels.get(0);
        assert hotel.get("id") != null;
    }

    @Test
    void testGetAllBookingsForAHotel() {
        ResponseEntity<List> response = restTemplate.exchange(createURLWithPort("/bookings/byHotelName/Mariott"), HttpMethod.GET, null, List.class);
        List<LinkedHashMap<String, String>> bookings = (List<LinkedHashMap<String, String>>) response.getBody();
        assert bookings != null;
        LinkedHashMap<String, String> booking = bookings.get(0);
        assert booking.get("id") != null;
    }

}
