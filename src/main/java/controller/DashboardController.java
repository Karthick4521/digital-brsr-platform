package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.service.DepartmentService;
import com.indiapost.brsrplatform.service.DocumentService;
import com.indiapost.brsrplatform.service.SustainabilityReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private SustainabilityReportService reportService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        // Real statistics from database
        long totalReports = reportService.countAll();
        long pendingReports = reportService.countByStatus("SUBMITTED");
        long approvedReports = reportService.countByStatus("APPROVED");
        long rejectedReports = reportService.countByStatus("REJECTED");
        long totalDepartments = departmentService.getAllActiveDepartments().size();
        long totalDocuments = documentService.getAllDocuments().size();

        model.addAttribute("totalReports", totalReports);
        model.addAttribute("pendingReports", pendingReports);
        model.addAttribute("approvedReports", approvedReports);
        model.addAttribute("rejectedReports", rejectedReports);
        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("totalDocuments", totalDocuments);
        model.addAttribute("recentReports",
                reportService.getAllReports());
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        model.addAttribute("username", authentication.getName());

        return "dashboard/index";
    }
}