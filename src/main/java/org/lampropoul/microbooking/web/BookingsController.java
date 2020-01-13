package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Booking;
import org.lampropoul.microbooking.repositories.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingRepository bookingRepository;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Booking>> all() {
        return new ResponseEntity<>(bookingRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Booking>> byId(@PathVariable Long id) {
        return new ResponseEntity<>(bookingRepository.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        if (bookingRepository.findById(booking.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(bookingRepository.save(booking), HttpStatus.CREATED);
        }
    }

    @PutMapping
    public ResponseEntity<Booking> update(@RequestBody Booking booking) {
        if (bookingRepository.findById(booking.getId()).isPresent()) {
            return new ResponseEntity<>(bookingRepository.save(booking), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> delete(@PathVariable Long id) {
        if (bookingRepository.findById(id).isPresent()) {
            bookingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/byHotelName/{hotelName}")
    public ResponseEntity<List<Booking>> getAllBookingsForAHotel(@PathVariable String hotelName) {
        return new ResponseEntity<>(bookingRepository.getAllByHotel(hotelName), HttpStatus.OK);
    }
}
