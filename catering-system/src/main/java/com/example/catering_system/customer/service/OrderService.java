package com.example.catering_system.customer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catering_system.customer.controller.OrderController.OrderCreationRequest;
import com.example.catering_system.customer.controller.OrderController.OrderItemRequest;
import com.example.catering_system.customer.model.Customer;
import com.example.catering_system.customer.model.Order;
import com.example.catering_system.customer.model.OrderItem;
import com.example.catering_system.customer.repository.CustomerRepository;
import com.example.catering_system.customer.repository.OrderRepository;

/**
 * Service class for Order management
 */
@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Generate order number if not provided
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }
        
        return orderRepository.save(order);
    }
    
    public Order createOrderFromRequest(OrderCreationRequest request) {
        // Find customer
        Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
        if (!customerOpt.isPresent()) {
            throw new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId());
        }
        
        Customer customer = customerOpt.get();
        
        // Create new order
        Order order = new Order();
        order.setCustomer(customer);
        // Set denormalized customer name for DB column customer_name
        String name = customer.getFullName();
        if (name == null || name.isBlank()) {
            name = customer.getEmail();
        }
        order.setCustomerName(name);
        order.setOrderType(Order.OrderType.valueOf(request.getOrderType()));
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Set delivery information if applicable
        if (request.getOrderType().equals("DELIVERY") || request.getOrderType().equals("CATERING")) {
            String deliveryAddr = request.getDeliveryAddress();
            order.setDeliveryAddress(deliveryAddr);
            // Set dropoff address (required by database)
            // Use delivery address if provided, otherwise use customer's address or default
            if (deliveryAddr != null && !deliveryAddr.trim().isEmpty()) {
                order.setDropoffAddress(deliveryAddr);
            } else {
                // Use customer's primary address if available
                String customerAddr = customer.getPrimaryAddress();
                if (customerAddr != null && !customerAddr.trim().isEmpty()) {
                    order.setDropoffAddress(customerAddr);
                    order.setDeliveryAddress(customerAddr);
                } else {
                    // Default fallback
                    order.setDropoffAddress("To be confirmed");
                }
            }
        } else {
            // For non-delivery orders, set a default dropoff address
            order.setDropoffAddress("N/A - " + request.getOrderType());
        }
        
        // Set pickup address (required by database)
        // For all order types, set a pickup address
        if (request.getOrderType().equals("PICKUP")) {
            // For pickup orders, use restaurant address or customer address
            String customerAddr = customer.getPrimaryAddress();
            if (customerAddr != null && !customerAddr.trim().isEmpty()) {
                order.setPickupAddress(customerAddr);
            } else {
                order.setPickupAddress("Restaurant Location");
            }
        } else {
            // For delivery/catering/dine-in, use restaurant as pickup location
            order.setPickupAddress("Restaurant Location");
        }
        
        // Set special instructions
        order.setSpecialInstructions(request.getSpecialInstructions());
        
        // Generate order number
        order.setOrderNumber(generateOrderNumber());
        
        // Calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            BigDecimal itemTotal = BigDecimal.valueOf(itemRequest.getPrice())
                .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            subtotal = subtotal.add(itemTotal);
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setItemName(itemRequest.getName());
            orderItem.setUnitPrice(BigDecimal.valueOf(itemRequest.getPrice()));
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setTotalPrice(itemTotal);
            order.addOrderItem(orderItem);
        }
        
        order.setSubtotal(subtotal);
        
        // Calculate tax (8%)
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(0.08));
        order.setTaxAmount(taxAmount);
        
        // Calculate delivery fee
        BigDecimal deliveryFee = BigDecimal.ZERO;
        if (request.getOrderType().equals("DELIVERY") || request.getOrderType().equals("CATERING")) {
            deliveryFee = BigDecimal.valueOf(5.00);
        }
        order.setDeliveryFee(deliveryFee);
        
        // Calculate total
        BigDecimal total = subtotal.add(taxAmount).add(deliveryFee);
        order.setTotalAmount(total);
        
        return orderRepository.save(order);
    }
    
    public Order updateOrder(Order orderUpdates) {
        // Fetch existing order
        Order existingOrder = orderRepository.findById(orderUpdates.getId())
            .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderUpdates.getId()));
        
        // Update only the fields that are provided
        if (orderUpdates.getDeliveryAddress() != null) {
            existingOrder.setDeliveryAddress(orderUpdates.getDeliveryAddress());
            // Also update dropoff address if delivery address changed
            existingOrder.setDropoffAddress(orderUpdates.getDeliveryAddress());
        }
        
        if (orderUpdates.getSpecialInstructions() != null) {
            existingOrder.setSpecialInstructions(orderUpdates.getSpecialInstructions());
        }
        
        if (orderUpdates.getStatus() != null) {
            existingOrder.setStatus(orderUpdates.getStatus());
            // Update delivery date if status is DELIVERED
            if (orderUpdates.getStatus() == Order.OrderStatus.DELIVERED && existingOrder.getActualDeliveryDate() == null) {
                existingOrder.setActualDeliveryDate(LocalDateTime.now());
            }
        }
        
        existingOrder.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(existingOrder);
    }
    
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }
    
    public List<Order> findOrdersByType(Order.OrderType orderType) {
        return orderRepository.findByOrderType(orderType);
    }
    
    public List<Order> findOrdersByPaymentStatus(Order.PaymentStatus paymentStatus) {
        return orderRepository.findByPaymentStatus(paymentStatus);
    }
    
    public List<Order> findOrdersByDateRange(String startDate, String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        return orderRepository.findOrdersByDateRange(start, end);
    }
    
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        // Update delivery date based on status
        if (newStatus == Order.OrderStatus.DELIVERED && order.getActualDeliveryDate() == null) {
            order.setActualDeliveryDate(LocalDateTime.now());
        }
        
        return orderRepository.save(order);
    }
    
    public RevenueStats getRevenueStatistics() {
        // Get total revenue from delivered orders
        Double totalRevenue = orderRepository.getTotalRevenueSince(LocalDateTime.now().minusYears(1));
        if (totalRevenue == null) totalRevenue = 0.0;
        
        // Get total orders count
        long totalOrders = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        
        // Calculate average order value
        Double averageOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;
        
        return new RevenueStats(totalRevenue, totalOrders, averageOrderValue);
    }
    
    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String datePrefix = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timeSuffix = String.format("%04d", now.getHour() * 100 + now.getMinute());
        return "ORD-" + datePrefix + "-" + timeSuffix;
    }
    
    // Helper class for revenue statistics
    public static class RevenueStats {
        private Double totalRevenue;
        private Long totalOrders;
        private Double averageOrderValue;
        
        public RevenueStats() {}
        
        public RevenueStats(Double totalRevenue, Long totalOrders, Double averageOrderValue) {
            this.totalRevenue = totalRevenue;
            this.totalOrders = totalOrders;
            this.averageOrderValue = averageOrderValue;
        }
        
        // Getters and setters
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }
        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }
    
    public long getActiveOrdersCount() {
        long pending = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        long confirmed = orderRepository.countByStatus(Order.OrderStatus.CONFIRMED);
        long preparing = orderRepository.countByStatus(Order.OrderStatus.PREPARING);
        return pending + confirmed + preparing;
    }
    
    // Analytics methods
    public long getOrdersSince(LocalDateTime since) {
        return orderRepository.countOrdersSince(since);
    }
    
    public Double getTotalRevenueAllSince(LocalDateTime since) {
        Double revenue = orderRepository.getTotalRevenueAllSince(since);
        return revenue != null ? revenue : 0.0;
    }
    
    public Double getAverageOrderValueSince(LocalDateTime since) {
        Double avg = orderRepository.getAverageOrderValueSince(since);
        return avg != null ? avg : 0.0;
    }
    
    public List<Object[]> getOrderTypeStatsSince(LocalDateTime since) {
        return orderRepository.getOrderTypeStatsSince(since);
    }
    
    public List<Object[]> getOrderStatusStatsSince(LocalDateTime since) {
        return orderRepository.getOrderStatusStatsSince(since);
    }
    
    public List<Order> getTopOrdersByValueSince(LocalDateTime since, int limit) {
        return orderRepository.getTopOrdersByValueSince(since).stream()
            .limit(limit)
            .collect(java.util.stream.Collectors.toList());
    }
}
