package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelRepository hotelRepository;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Hotel>> all() {
        return new ResponseEntity<>(hotelRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> byId(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        return hotel
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        if (hotelRepository.findById(hotel.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(hotelRepository.save(hotel), HttpStatus.CREATED);
        }
    }

    @PutMapping
    public ResponseEntity<Hotel> update(@RequestBody Hotel hotel) {
        if (hotelRepository.findById(hotel.getId()).isPresent()) {
            return new ResponseEntity<>(hotelRepository.save(hotel), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Hotel> delete(@PathVariable Long id) {
        if (hotelRepository.findById(id).isPresent()) {
            hotelRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/withBookingsFor/{surname}")
    public ResponseEntity<List<Hotel>> hotelsThatHaveBookingsForSurname(@PathVariable String surname) {
        List<Hotel> hotels = hotelRepository.findAllBySurname(surname);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @GetMapping("/{id}/priceAmounts")
    public ResponseEntity<Float> priceAmounts(@PathVariable Long id) {
        return new ResponseEntity<>(hotelRepository.calculatePriceAmounts(id), HttpStatus.OK);
    }
}
