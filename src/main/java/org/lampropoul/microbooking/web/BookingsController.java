package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Booking;
import org.lampropoul.microbooking.repositories.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingRepository bookingRepository;

    @GetMapping("/{hotelName}")
    public ResponseEntity<List<Booking>> getAllBookingsForAHotel(@PathVariable String hotelName) {
        return new ResponseEntity<>(bookingRepository.getAllByHotel(hotelName), HttpStatus.OK);
    }
}
