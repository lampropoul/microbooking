package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelRepository hotelRepository;

    @GetMapping("/bookingsFor/{surname}")
    public ResponseEntity<List<Hotel>> all(@PathVariable String surname) {
        List<Hotel> hotels = hotelRepository.findAllBySurname(surname);
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }
}
