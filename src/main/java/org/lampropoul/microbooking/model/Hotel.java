package org.lampropoul.microbooking.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Hotel {
    @Id
    private long id;
    private String name;
    private String address;
    private int rating;
}
