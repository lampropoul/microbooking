# microbooking
Micro Booking including only hotels and bookings

### Run using docker
docker run -it --rm --name microbooking-container -v "$PWD":/usr/src/mymaven -w /usr/src/mymaven -p 8080:8080 openjdk:11 ./mvnw test spring-boot:run
