package org.lampropoul.microbooking.model;

import lombok.Data;

@Data
public class Hotel {
    private long id;
    private String name;
    private String address;
    private int rating;
}
