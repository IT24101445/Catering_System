package com.example.catering_system.controller;

import com.example.catering_system.model.MenuItem;
import com.example.catering_system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu")
    public String showMenu(Model model) {
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "menu";  // Ensure this is mapped to the menu.html template
    }

    @PostMapping("/menu/add")
    public String addMenuItem(@RequestParam String name, @RequestParam String description,
                              @RequestParam double price, @RequestParam String imageUrl, @RequestParam String category) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setImageUrl(imageUrl);
        menuItem.setCategory(category);

        menuService.addMenuItem(menuItem);

        return "redirect:/menu";  // Redirect to /menu after adding the item
    }
}
