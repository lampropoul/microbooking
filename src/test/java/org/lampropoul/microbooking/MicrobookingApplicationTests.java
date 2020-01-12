package org.lampropoul.microbooking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MicrobookingApplicationTests {

    final TestRestTemplate restTemplate = new TestRestTemplate();
    private final String baseURL = "/hotels";
    @LocalServerPort
    private int port;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + baseURL + uri;
    }

    @Test
    void testGetAllBySurname() {
        ResponseEntity<Iterable> response = restTemplate.exchange(createURLWithPort("/Lambropoulos"), HttpMethod.GET, null, Iterable.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
    }

}
