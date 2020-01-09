package org.lampropoul.microbooking.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Booking {
    @Id
    private long id;
    private String customerName;
    private String customerSurname;
    private int pax;
}
