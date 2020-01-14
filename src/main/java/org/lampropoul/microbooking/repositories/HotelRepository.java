package org.lampropoul.microbooking.repositories;

import org.lampropoul.microbooking.model.Hotel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends CrudRepository<Hotel, Long> {

    @Query(value = "select distinct h from Booking b inner join Hotel h on b.hotel.id = h.id where b.customerSurname = ?1")
    List<Hotel> findAllBySurname(String surname);

    @Query(value = "select sum(b.price) from Booking b where b.hotel.id = ?1")
    float calculatePriceAmounts(Long id);
}
