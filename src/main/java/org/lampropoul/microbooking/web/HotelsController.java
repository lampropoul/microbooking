package org.lampropoul.microbooking.web;

import lombok.RequiredArgsConstructor;
import org.lampropoul.microbooking.model.Hotel;
import org.lampropoul.microbooking.repositories.HotelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelRepository hotelRepository;

    @GetMapping("/{surname}")
    public Iterable<Hotel> all(@PathVariable String surname) {
        return hotelRepository.findAllBySurname(surname);
    }
}
