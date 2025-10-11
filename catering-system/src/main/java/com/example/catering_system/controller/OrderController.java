package com.example.catering_system.controller;

import com.example.catering_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/pending")
    public String pending(Model model) {
        model.addAttribute("orders", orderService.getPending());
        return "pending-orders";
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("orders", orderService.getAll());
        return "orders-history";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.markStatus(id, status);
        return "redirect:/orders/pending";
    }
}


