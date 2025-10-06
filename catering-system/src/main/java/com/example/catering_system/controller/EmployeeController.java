package com.example.catering_system.controller;

import com.example.catering_system.model.Employee;
import com.example.catering_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees-list")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-add";
    }

    @PostMapping("/add")
    public String createEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "employee-add";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees-list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id).orElse(null));
        return "employee-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "employee-edit";
        }
        employeeService.updateEmployee(id, employee);
        return "redirect:/employees-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees-list";
    }

    // Base salary endpoint to help auto-fill in add payment form
    @GetMapping("/base-salary")
    @ResponseBody
    public Double getBaseSalary(@RequestParam Long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .map(e -> e.getBaseSalary() != null ? e.getBaseSalary() : 0.0)
                .orElse(0.0);
    }
}
