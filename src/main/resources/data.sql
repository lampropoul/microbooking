INSERT INTO hotel (id, name, address, rating)
VALUES (1, 'Hyatt Place', '56 W 36th St, New York, NY', 4),
       (2, 'Mariott', '56 W 37th St, New York, NY', 5);

INSERT INTO booking (id, customer_name, customer_surname, pax, hotel_id, price, currency)
VALUES (3, 'Vassilis', 'Lambropoulos', 1, 1, 1000, 'USD'),
       (4, 'Konstantina', 'Lambropoulou', 1, 1, 2484.17, 'EUR'),
       (5, 'Vassilis', 'Lambropoulos', 2, 2, 1990.10, 'USD'),
       (6, 'Konstantina', 'Lambropoulou', 2, 2, 1989.12, 'EUR');