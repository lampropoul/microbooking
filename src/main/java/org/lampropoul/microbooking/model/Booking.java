package org.lampropoul.microbooking.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String customerName;
    private String customerSurname;
    private int pax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false, updatable = false)
    private Hotel hotel;
}