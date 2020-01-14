package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Booking;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.BookingRepository;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Web controller that accepts requests on /bookings
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;

    /**
     * @return all bookings on DB and HTTP status of 200 OK
     */
    @GetMapping("/all")
    public ResponseEntity<Iterable<Booking>> all() {
        return new ResponseEntity<>(bookingRepository.findAll(), HttpStatus.OK);
    }

    /**
     * @param id primary key
     * @return a booking and HTTP status of 200 OK or 204 NO_CONTENT
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> byId(@PathVariable Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * @param booking request object in HTTP body
     * @return a booking and HTTP status of 201 CREATED or 400 BAD_REQUEST
     */
    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        if (bookingRepository.findById(booking.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (hotelIsNull(booking)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(bookingRepository.save(booking), HttpStatus.CREATED);
        }
    }

    /**
     * @param booking request object in HTTP body
     * @return a booking and HTTP status of 200 OK or 400 BAD_REQUEST
     */
    @PutMapping
    public ResponseEntity<Booking> update(@RequestBody Booking booking) {
        if (bookingRepository.findById(booking.getId()).isPresent()) {
            if (hotelIsNull(booking)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(bookingRepository.save(booking), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param id the primary key
     * @return a booking and HTTP status of 204 NO_CONTENT or 400 BAD_REQUEST
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> delete(@PathVariable Long id) {
        if (bookingRepository.findById(id).isPresent()) {
            bookingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param hotelName name of the hotel
     * @return a list with all the booking that this hotel has and HTTP status of 200 OK
     */
    @GetMapping("/byHotelName/{hotelName}")
    public ResponseEntity<List<Booking>> getAllBookingsForAHotel(@PathVariable String hotelName) {
        return new ResponseEntity<>(bookingRepository.getAllByHotel(hotelName), HttpStatus.OK);
    }

    /**
     * @param booking a booking object
     * @return true if hotel is null, false if the hotel is either new or exists
     * in the case that the hotel does not exist, it gets persisted
     */
    private boolean hotelIsNull(Booking booking) {
        Hotel hotel = booking.getHotel();
        if (hotel == null) {
            return true;
        }
        if (hotelRepository.findById(hotel.getId()).isEmpty()) {
            Hotel persistedHotel = hotelRepository.save(hotel);
            booking.setHotel(persistedHotel);
        }
        return false;
    }
}
