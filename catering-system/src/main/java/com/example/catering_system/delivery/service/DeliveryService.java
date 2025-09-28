package com.example.catering_system.delivery.service;



import com.example.catering_system.delivery.models.Delivery;
import com.example.catering_system.delivery.models.DeliveryRoute;
import com.example.catering_system.delivery.models.Driver;
import com.example.catering_system.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepo;

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private DeliveryRouteRepository routeRepo;

    // Assign driver to delivery
    public boolean assignDriver(Long deliveryId, Long driverId) {
        Optional<Delivery> deliveryOpt = deliveryRepo.findById(deliveryId);
        Optional<Driver> driverOpt = driverRepo.findById(driverId);

        if (deliveryOpt.isEmpty() || driverOpt.isEmpty()) return false;

        Delivery delivery = deliveryOpt.get();
        Driver driver = driverOpt.get();

        if (!driver.isAvailable()) return false;

        delivery.setDriverId(driverId);
        delivery.setStatus("ASSIGNED");
        deliveryRepo.save(delivery);

        driver.setAvailable(false);
        driverRepo.save(driver);

        return true;
    }

    // Start delivery
    public boolean startDelivery(Long deliveryId) {
        Optional<Delivery> deliveryOpt = deliveryRepo.findById(deliveryId);
        if (deliveryOpt.isEmpty()) return false;

        Delivery delivery = deliveryOpt.get();
        if (!"ASSIGNED".equals(delivery.getStatus())) return false;

        delivery.startDelivery(); // sets status = "IN_TRANSIT"
        deliveryRepo.save(delivery);

        return true;
    }

    // Complete delivery
    public boolean completeDelivery(Long deliveryId) {
        Optional<Delivery> deliveryOpt = deliveryRepo.findById(deliveryId);
        if (deliveryOpt.isEmpty()) return false;

        Delivery delivery = deliveryOpt.get();
        if (!"IN_TRANSIT".equals(delivery.getStatus())) return false;

        delivery.completeDelivery(); // sets status = "COMPLETED"
        deliveryRepo.save(delivery);

        // Free up driver
        if (delivery.getDriverId() != null) {
            Optional<Driver> driverOpt = driverRepo.findById(delivery.getDriverId());
            if (driverOpt.isPresent()) {
                Driver driver = driverOpt.get();
                driver.setAvailable(true);
                driverRepo.save(driver);
            }
        }

        return true;
    }

    // Get all deliveries by status
    public List<Delivery> getDeliveriesByStatus(String status) {
        return deliveryRepo.findByStatus(status);
    }

    // Get available drivers
    public List<Driver> getAvailableDrivers() {
        return driverRepo.findByIsAvailableTrue();
    }

    // Save delivery route (mock for now)
    public void saveRoute(Long deliveryId, String startPoint, String endPoint, double distanceKm, int estimatedTimeMin) {
        DeliveryRoute route = new DeliveryRoute(deliveryId, startPoint, endPoint, distanceKm, estimatedTimeMin);
        routeRepo.save(route);
    }

    // Get route for delivery
    public DeliveryRoute getRouteByDeliveryId(Long deliveryId) {
        return routeRepo.findByDeliveryId(deliveryId);
    }
}
