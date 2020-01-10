INSERT INTO hotel (id, name, address, rating)
VALUES (1, 'Hyatt Place', '56 W 36th St, New York, NY', 4);
INSERT INTO hotel (id, name, address, rating)
VALUES (2, 'Mariott', '56 W 37th St, New York, NY', 5);

INSERT INTO booking (id, customer_name, customer_surname, pax, hotel_id)
VALUES (3, 'Vassilis', 'Lambropoulos', 1, 1),
       (4, 'Konstantina', 'Lambropoulos', 1, 1),
       (5, 'Vassilis', 'Lambropoulos', 2, 2),
       (6, 'Konstantina', 'Lambropoulos', 2, 2);