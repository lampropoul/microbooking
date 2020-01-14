package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Web controller that accepts requests on /hotels
 */
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelRepository hotelRepository;

    /**
     * @return all hotels on DB and HTTP status of 200 OK
     */
    @GetMapping("/all")
    public ResponseEntity<Iterable<Hotel>> all() {
        return new ResponseEntity<>(hotelRepository.findAll(), HttpStatus.OK);
    }

    /**
     * @param id primary key
     * @return a hotel and HTTP status of 200 OK or 204 NO_CONTENT
     */
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> byId(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        return hotel
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * @param hotel request object in HTTP body
     * @return a hotel and HTTP status of 201 CREATED or 400 BAD_REQUEST
     */
    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        if (hotelRepository.findById(hotel.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(hotelRepository.save(hotel), HttpStatus.CREATED);
        }
    }

    /**
     * @param hotel request object in HTTP body
     * @return a hotel and HTTP status of 200 OK or 400 BAD_REQUEST
     */
    @PutMapping
    public ResponseEntity<Hotel> update(@RequestBody Hotel hotel) {
        if (hotelRepository.findById(hotel.getId()).isPresent()) {
            return new ResponseEntity<>(hotelRepository.save(hotel), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param id the primary key
     * @return a hotel and HTTP status of 204 NO_CONTENT or 400 BAD_REQUEST
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Hotel> delete(@PathVariable Long id) {
        if (hotelRepository.findById(id).isPresent()) {
            hotelRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param surname a client's surname
     * @return get all hotels this client has booked for
     */
    @GetMapping("/withBookingsFor/{surname}")
    public ResponseEntity<List<Hotel>> hotelsThatHaveBookingsForSurname(@PathVariable String surname) {
        List<Hotel> hotels = hotelRepository.findAllBySurname(surname);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    /**
     * @param id hotel id
     * @return the sum of the amounts of all bookings this hotel has
     */
    @GetMapping("/{id}/priceAmounts")
    public ResponseEntity<Float> priceAmounts(@PathVariable Long id) {
        return new ResponseEntity<>(hotelRepository.calculatePriceAmounts(id), HttpStatus.OK);
    }
}
