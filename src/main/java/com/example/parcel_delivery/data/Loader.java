package com.example.parcel_delivery.data;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.parcel_delivery.models.entities.Cabinet;
import com.example.parcel_delivery.models.entities.Customer;
import com.example.parcel_delivery.models.entities.Driver;
import com.example.parcel_delivery.models.entities.ParcelLocker;
import com.example.parcel_delivery.models.entities.User;
import com.example.parcel_delivery.models.enums.CabinetStatus;
import com.example.parcel_delivery.models.enums.DriverType;
import com.example.parcel_delivery.repositories.CabinetRepo;
import com.example.parcel_delivery.repositories.CustomerRepo;
import com.example.parcel_delivery.repositories.DriverRepo;
import com.example.parcel_delivery.repositories.ParcelLockerRepo;
import com.example.parcel_delivery.repositories.UserRepo;

@Component
public class Loader implements CommandLineRunner {

    @Autowired
    private ParcelLockerRepo parcelLockerRepo;

    @Autowired
    private CabinetRepo cabinetRepo;

    @Autowired
    private CustomerRepo customerRepository; 

    @Autowired
    private UserRepo userRepository;
    
    @Autowired
    private DriverRepo driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




  
    private static final double OUlu_LAT = 65.01236;
    private static final double OUlu_LON = 25.46816;
    private static final double RADIUS = 35000; // 35 km in meters
    private static final List<String> FINNISH_CITIES = Arrays.asList("Helsinki", "Espoo", "Tampere", "Vantaa", "Oulu");
    private static final int DRIVERS_PER_CITY = 5;
    private static final List<DriverType> DRIVER_TYPES = Arrays.asList(DriverType.INTER_CITY, DriverType.INTRA_CITY);
    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        generateParcelLockers();
        generateRecipients(10);
        generateDriversInCities();
    }


    private void generateParcelLockers() {
         GeometryFactory geometryFactory = new GeometryFactory();
        Random random = new Random();

        for (int i = 1; i <= 30; i++) {
            // Generate random point within 35 km radius of Oulu city center
            double[] randomPoint = generateRandomPoint(OUlu_LAT, OUlu_LON, RADIUS, random);
            Point location = geometryFactory.createPoint(new Coordinate(randomPoint[1], randomPoint[0]));
            location.setSRID(4326);

            ParcelLocker locker = new ParcelLocker();
            locker.setName("Locker " + i);
            locker.setLockerPoint(location);

            Set<Cabinet> cabinets = new HashSet<>();
            for (int j = 1; j <= 10; j++) {
                Cabinet cabinet = new Cabinet();
                cabinet.setStatus(CabinetStatus.FREE);
                cabinet.setWidth(50.0);
                cabinet.setHeight(50.0);
                cabinet.setDepth(50.0);
                cabinet.setLockerLocation(locker);
                cabinets.add(cabinet);
            }
            locker.setCabinets(cabinets);

            parcelLockerRepo.save(locker);
            cabinetRepo.saveAll(cabinets);
        }
    }


    private double[] generateRandomPoint(double latitude, double longitude, double radius, Random random) {
        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(latitude));

        double foundLongitude = new_x + longitude;
        double foundLatitude = y + latitude;

        return new double[]{foundLatitude, foundLongitude};
    }

       private void generateRecipients(int numberOfRecipients) {
        for (int i = 0; i < numberOfRecipients; i++) {
            User user = createUser("recipient" + i, "Some Street " + i, "Helsinki");
            Customer customer = new Customer();
            customer.setUser(user);
            customerRepository.save(customer);
        }
    }

    private void generateDriversInCities() {
        FINNISH_CITIES.forEach(city -> {
            for (int i = 0; i < DRIVERS_PER_CITY; i++) {
                DriverType driverType = DRIVER_TYPES.get(random.nextInt(DRIVER_TYPES.size()));
                createDriverUser(city, driverType);
            }
        });
    }

    private void createDriverUser(String city, DriverType driverType) {
        User driverUser = createUser("driver" + city + random.nextInt(1000), "Driver Street " + random.nextInt(100), city);
        Driver driver = new Driver();
        driver.setUser(driverUser);
        driver.setDriverType(driverType);
        driver.setIsAvailable(true);
        driverRepository.save(driver);
    }

    private User createUser(String username, String address, String city) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName(username + "FirstName");
        user.setLastName(username + "LastName");
        user.setEmail(username + "@example.com");
        user.setPhoneNumber("050" + (1000000 + random.nextInt(9000000)));
        user.setAddress(address);
        user.setCity(city);
        user.setPostcode("00100");
        return userRepository.save(user);
    }
}