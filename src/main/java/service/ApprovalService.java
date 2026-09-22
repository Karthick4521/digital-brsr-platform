package com.indiapost.brsrplatform.service;

import com.indiapost.brsrplatform.entity.Approval;
import com.indiapost.brsrplatform.entity.SustainabilityReport;
import com.indiapost.brsrplatform.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private SustainabilityReportService reportService;

    public List<Approval> getApprovalsByReport(Long reportId) {
        return approvalRepository.findByReportId(reportId);
    }

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    public void submitReport(Long reportId) {
        SustainabilityReport report = reportService.getReportById(reportId);
        if (report != null) {
            report.setStatus("SUBMITTED");
            reportService.saveReport(report);
        }
    }

    public void approveReport(Long reportId, Long reviewedBy, String comments) {
        SustainabilityReport report = reportService.getReportById(reportId);
        if (report != null) {
            report.setStatus("APPROVED");
            reportService.saveReport(report);
            Approval approval = new Approval();
            approval.setReportId(reportId);
            approval.setReviewedBy(reviewedBy);
            approval.setAction("APPROVED");
            approval.setComments(comments);
            approvalRepository.save(approval);
        }
    }

    public void rejectReport(Long reportId, Long reviewedBy, String comments) {
        SustainabilityReport report = reportService.getReportById(reportId);
        if (report != null) {
            report.setStatus("REJECTED");
            reportService.saveReport(report);
            Approval approval = new Approval();
            approval.setReportId(reportId);
            approval.setReviewedBy(reviewedBy);
            approval.setAction("REJECTED");
            approval.setComments(comments);
            approvalRepository.save(approval);
        }
    }
}