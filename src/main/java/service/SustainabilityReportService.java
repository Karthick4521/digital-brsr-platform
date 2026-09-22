package com.indiapost.brsrplatform.service;

import com.indiapost.brsrplatform.entity.ReportDetails;
import com.indiapost.brsrplatform.entity.SustainabilityReport;
import com.indiapost.brsrplatform.repository.ReportDetailsRepository;
import com.indiapost.brsrplatform.repository.SustainabilityReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SustainabilityReportService {

    @Autowired
    private SustainabilityReportRepository reportRepository;

    @Autowired
    private ReportDetailsRepository detailsRepository;

    public List<SustainabilityReport> getAllReports() {
        return reportRepository.findAll();
    }

    public List<SustainabilityReport> getReportsByDepartment(Long deptId) {
        return reportRepository.findByDepartmentId(deptId);
    }

    public List<SustainabilityReport> getReportsByStatus(String status) {
        return reportRepository.findByStatus(status);
    }

    public List<SustainabilityReport> getReportsByUser(Long userId) {
        return reportRepository.findBySubmittedBy(userId);
    }

    public SustainabilityReport getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public SustainabilityReport saveReport(SustainabilityReport report) {
        report.setUpdatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public void saveReportDetails(ReportDetails details) {
        detailsRepository.save(details);
    }

    public ReportDetails getDetailsByReportId(Long reportId) {
        return detailsRepository.findByReportId(reportId).orElse(null);
    }

    public long countByStatus(String status) {
        return reportRepository.countByStatus(status);
    }

    public long countAll() {
        return reportRepository.count();
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}