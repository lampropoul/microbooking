package org.lampropoul.microbooking.model;

import lombok.Data;

@Data
public class Booking {
    private long id;
    private String customerName;
    private String customerSurname;
    private int pax;
}
