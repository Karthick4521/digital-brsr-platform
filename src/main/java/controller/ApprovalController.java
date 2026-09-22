package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.entity.User;
import com.indiapost.brsrplatform.service.ApprovalService;
import com.indiapost.brsrplatform.service.SustainabilityReportService;
import com.indiapost.brsrplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private SustainabilityReportService reportService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listApprovals(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("approvals", approvalService.getAllApprovals());
        return "approval/list";
    }

    @GetMapping("/submit/{reportId}")
    public String submitReport(@PathVariable Long reportId,
                               RedirectAttributes redirectAttributes) {
        approvalService.submitReport(reportId);
        redirectAttributes.addFlashAttribute("successMsg",
                "Report submitted for approval successfully!");
        return "redirect:/approvals";
    }

    @GetMapping("/review/{reportId}")
    public String showReviewPage(@PathVariable Long reportId, Model model) {
        model.addAttribute("report", reportService.getReportById(reportId));
        model.addAttribute("approvals",
                approvalService.getApprovalsByReport(reportId));
        return "approval/review";
    }

    @PostMapping("/approve/{reportId}")
    public String approveReport(@PathVariable Long reportId,
                                @RequestParam String comments,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName());
        Long reviewedBy = user != null ? user.getId() : 1L;
        approvalService.approveReport(reportId, reviewedBy, comments);
        redirectAttributes.addFlashAttribute("successMsg",
                "Report approved successfully!");
        return "redirect:/approvals";
    }

    @PostMapping("/reject/{reportId}")
    public String rejectReport(@PathVariable Long reportId,
                               @RequestParam String comments,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName());
        Long reviewedBy = user != null ? user.getId() : 1L;
        approvalService.rejectReport(reportId, reviewedBy, comments);
        redirectAttributes.addFlashAttribute("successMsg",
                "Report rejected successfully!");
        return "redirect:/approvals";
    }
}