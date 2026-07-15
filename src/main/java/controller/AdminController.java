package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.entity.User;
import com.indiapost.brsrplatform.service.DepartmentService;
import com.indiapost.brsrplatform.service.SustainabilityReportService;
import com.indiapost.brsrplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private SustainabilityReportService reportService;

    @Autowired
    private DepartmentService departmentService;

    // Admin Dashboard
    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers",
                userService.getAllUsers().size());
        model.addAttribute("totalReports",
                reportService.countAll());
        model.addAttribute("pendingReports",
                reportService.countByStatus("SUBMITTED"));
        model.addAttribute("approvedReports",
                reportService.countByStatus("APPROVED"));
        model.addAttribute("rejectedReports",
                reportService.countByStatus("REJECTED"));
        model.addAttribute("recentReports",
                reportService.getAllReports());
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        return "admin/dashboard";
    }

    // View All Users
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // View User Reports
    @GetMapping("/users/{id}/reports")
    public String viewUserReports(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) return "redirect:/admin/users";
        model.addAttribute("user", user);
        model.addAttribute("reports",
                reportService.getReportsByUser(id));
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        return "admin/user-reports";
    }

    // Make Admin
    @GetMapping("/users/{id}/make-admin")
    public String makeAdmin(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        userService.makeAdmin(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "User promoted to Admin successfully!");
        return "redirect:/admin/users";
    }

    // Make User
    @GetMapping("/users/{id}/make-user")
    public String makeUser(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        userService.makeUser(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "User demoted to User role successfully!");
        return "redirect:/admin/users";
    }

    // Deactivate User
    @GetMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "User deactivated successfully!");
        return "redirect:/admin/users";
    }

    // Edit User Report (Admin only)
    @GetMapping("/reports/{id}/edit")
    public String editReport(@PathVariable Long id, Model model) {
        model.addAttribute("report",
                reportService.getReportById(id));
        model.addAttribute("details",
                reportService.getDetailsByReportId(id));
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        return "sustainability/form";
    }

    // View All Reports
    @GetMapping("/reports")
    public String viewAllReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        return "admin/all-reports";
    }
}