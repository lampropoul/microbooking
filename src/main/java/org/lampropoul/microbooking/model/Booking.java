package org.lampropoul.microbooking.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String customerName;
    private String customerSurname;
    private int pax;
    private float price;
    private String currency;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false, updatable = false)
    private Hotel hotel;
}
