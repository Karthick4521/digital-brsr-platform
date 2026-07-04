package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.entity.ReportDetails;
import com.indiapost.brsrplatform.entity.SustainabilityReport;
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
@RequestMapping("/reports")
public class SustainabilityController {

    @Autowired
    private SustainabilityReportService reportService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "sustainability/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("report", new SustainabilityReport());
        model.addAttribute("details", new ReportDetails());
        model.addAttribute("departments",
                departmentService.getAllActiveDepartments());
        return "sustainability/form";
    }

    @PostMapping("/save")
    public String saveReport(
            @ModelAttribute("report") SustainabilityReport report,
            @ModelAttribute("details") ReportDetails details,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(authentication.getName());
        if (user != null) {
            report.setSubmittedBy(user.getId());
        }
        report.setStatus("DRAFT");
        SustainabilityReport saved = reportService.saveReport(report);
        details.setReportId(saved.getId());
        reportService.saveReportDetails(details);
        redirectAttributes.addFlashAttribute("successMsg",
                "Report saved successfully!");
        return "redirect:/reports";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SustainabilityReport report = reportService.getReportById(id);
        if (report == null) return "redirect:/reports";

        ReportDetails details = reportService.getDetailsByReportId(id);
        if (details == null) details = new ReportDetails();

        model.addAttribute("report", report);
        model.addAttribute("details", details);
        model.addAttribute("departments",
                departmentService.getAllActiveDepartments());
        return "sustainability/form";
    }

    @PostMapping("/update")
    public String updateReport(
            @ModelAttribute("report") SustainabilityReport report,
            @ModelAttribute("details") ReportDetails details,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(authentication.getName());
        if (user != null) {
            report.setSubmittedBy(user.getId());
        }
        SustainabilityReport saved = reportService.saveReport(report);
        details.setReportId(saved.getId());
        reportService.saveReportDetails(details);
        redirectAttributes.addFlashAttribute("successMsg",
                "Report updated successfully!");
        return "redirect:/reports";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        reportService.deleteReport(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Report deleted successfully!");
        return "redirect:/reports";
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable Long id, Model model) {
        SustainabilityReport report = reportService.getReportById(id);
        if (report == null) return "redirect:/reports";

        ReportDetails details = reportService.getDetailsByReportId(id);
        model.addAttribute("report", report);
        model.addAttribute("details", details);
        model.addAttribute("departments",
                departmentService.getAllDepartments());
        return "sustainability/view";
    }
}