package org.lampropoul.microbooking.repositories;

import org.lampropoul.microbooking.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

    @Query("select b from Booking b where b.hotel.name = ?1")
    List<Booking> getAllByHotel(String hotelName);
}
