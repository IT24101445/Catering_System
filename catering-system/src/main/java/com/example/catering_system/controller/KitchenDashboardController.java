package com.example.catering_system.controller;

import com.example.catering_system.service.MenuService;
import com.example.catering_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KitchenDashboardController {

    @Autowired
    private MenuService menuService;
    @Autowired
    @Qualifier("mainOrderService")
    private OrderService orderService;

    @GetMapping("/kitchen-dashboard")
    public String kitchenDashboard(Model model,
                                   @RequestParam(required = false) String cuisine,
                                   @RequestParam(required = false) String eventType) {
        if (cuisine != null && !cuisine.isEmpty()) {
            model.addAttribute("menuItems", menuService.getByCuisine(cuisine));
        } else if (eventType != null && !eventType.isEmpty()) {
            model.addAttribute("menuItems", menuService.getByEventType(eventType));
        } else {
            model.addAttribute("menuItems", menuService.getAllMenuItems());
        }
        model.addAttribute("orders", orderService.getQueueByDeliveryTime());
        return "kitchen-dashboard"; // Renders kitchen-dashboard.html
    }

    @PostMapping("/kitchen/order/{id}/status")
    public String updateKitchenStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/kitchen-dashboard";
    }

    @GetMapping("/kitchen")
    public String kitchenAlias(Model model) {
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "kitchen-dashboard";
    }

    @PostMapping("/kitchen/menu/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return "redirect:/kitchen-dashboard";
    }
}
