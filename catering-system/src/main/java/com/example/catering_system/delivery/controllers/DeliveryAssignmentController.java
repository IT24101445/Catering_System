package com.example.catering_system.delivery.controllers;

import com.example.catering_system.delivery.dto.DeliverySupervisor.ApiError;
import com.example.catering_system.delivery.dto.DeliveryAssignment.Create;
import com.example.catering_system.delivery.dto.DeliveryAssignment.Update;
import com.example.catering_system.delivery.dto.DeliveryAssignment.Response;
import com.example.catering_system.delivery.models.DeliveryAssignment;
import com.example.catering_system.delivery.models.Driver;
import com.example.catering_system.delivery.models.Order;
import com.example.catering_system.delivery.service.DeliveryAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService service;

    public DeliveryAssignmentController(DeliveryAssignmentService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Create req) {
        try {
            DeliveryAssignment a = service.create(req.getOrderId(), req.getDriverId(), req.getRoute());
            Response body = toResponse(a);
            return ResponseEntity.created(URI.create("/api/assignments/" + a.getId())).body(body);
        } catch (NoSuchElementException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // READ (one)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            DeliveryAssignment a = service.get(id);
            return ResponseEntity.ok(toResponse(a));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Assignment not found"));
        }
    }

    // READ (all) + optional filters
    // /api/assignments?driverId=1 OR /api/assignments?orderId=2
    @GetMapping
    public ResponseEntity<List<Response>> list(
            @RequestParam(required = false) Long driverId,
            @RequestParam(required = false) Long orderId) {

        List<DeliveryAssignment> results;
        if (driverId != null) {
            results = service.listByDriver(driverId);
        } else if (orderId != null) {
            results = service.listByOrder(orderId);
        } else {
            results = service.list();
        }

        List<Response> body = results.stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    // UPDATE (partial)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Update req) {
        try {
            DeliveryAssignment a = service.update(id, req.getDriverId(), req.getOrderId(), req.getRoute());
            return ResponseEntity.ok(toResponse(a));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Assignment not found"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Assignment not found"));
        }
    }
    // ACTION: start route (sets driver status to ON_ROUTE)
    @PostMapping("/{id}/start")
    public ResponseEntity<?> start(@PathVariable("id") Long id,
                                   @RequestBody(required = false) com.example.catering_system.delivery.dto.DeliveryAssignment.Start req) {
        try {
            service.startRoute(id);
            // You can later use req.getLatitude()/getLongitude() for logging/telemetry
            var a = service.get(id);
            return ResponseEntity.ok(toResponse(a));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Assignment not found"));
        }
    }

    // ACTION: complete route (sets driver status to AVAILABLE)
    @PostMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable("id") Long id,
                                      @RequestBody(required = false) com.example.catering_system.delivery.dto.DeliveryAssignment.Complete req) {
        try {
            service.completeRoute(id);
            var a = service.get(id);
            return ResponseEntity.ok(toResponse(a));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Assignment not found"));
        }
    }

    // ACTION: reassign to another driver
    @PostMapping("/{id}/reassign")
    public ResponseEntity<?> reassign(@PathVariable("id") Long id,
                                      @RequestBody com.example.catering_system.delivery.dto.DeliveryAssignment.Reassign req) {
        try {
            var a = service.reassign(id, req.getNewDriverId());
            // You can log req.getNote() later if you add audit/events
            return ResponseEntity.ok(toResponse(a));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Assignment or driver not found"));
        }
    }


    private Response toResponse(DeliveryAssignment a) {
        Response dto = new Response();
        dto.setId(a.getId());
        dto.setRoute(a.getRoute());

        Driver d = a.getDriver();
        if (d != null) {
            dto.setDriverId(d.getId());
            dto.setDriverEmail(d.getEmail());
            dto.setDriverName(d.getName());
        }

        Order o = a.getOrder();
        if (o != null) {
            dto.setOrderId(o.getId());
            dto.setOrderCustomerName(o.getCustomerName());
        }

        return dto;
    }
}

