package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Booking;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.BookingRepository;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    @GetMapping("/{surname}")
    public Iterable<Hotel> all(@PathVariable String surname) {
        return hotelRepository.findAllBySurname(surname);
    }

    @GetMapping("/seed")
    public ResponseEntity<Booking> seed() {
        Hotel hotel = new Hotel();
        hotel.setName("Hyatt Place");
        hotel.setAddress("56 W 36th St, New York, NY");
        Hotel hyatt = hotelRepository.save(hotel);

        Booking booking = new Booking();
        booking.setCustomerName("Vassilis");
        booking.setCustomerSurname("Lambropoulos");
        booking.setHotel(hyatt);
        bookingRepository.save(booking);

        Booking booking2 = new Booking();
        booking2.setCustomerName("Konstantina");
        booking2.setCustomerSurname("Manika");
        booking2.setHotel(hyatt);

        return new ResponseEntity<>(bookingRepository.save(booking2), HttpStatus.OK);
    }
}
