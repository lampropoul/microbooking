package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Booking;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.BookingRepository;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    @GetMapping("/all")
    public Iterable<Hotel> all() {
        return hotelRepository.findAll();
    }

    @GetMapping("/seed")
    public ResponseEntity<Booking> seed() {
        Hotel hotel = new Hotel();
        hotel.setName("Hyatt");
        hotel.setAddress("56 W 36th St, New York, NY");
        Booking booking = new Booking();
        booking.setCustomerName("Vassilis");
        booking.setCustomerSurname("Lambropoulos");
        booking.setHotel(hotelRepository.save(hotel));
        return new ResponseEntity<>(bookingRepository.save(booking), HttpStatus.OK);
    }
}
