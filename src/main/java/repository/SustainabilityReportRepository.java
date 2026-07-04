package com.indiapost.brsrplatform.repository;

import com.indiapost.brsrplatform.entity.SustainabilityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SustainabilityReportRepository
        extends JpaRepository<SustainabilityReport, Long> {

    List<SustainabilityReport> findByDepartmentId(Long departmentId);

    List<SustainabilityReport> findByStatus(String status);

    List<SustainabilityReport> findBySubmittedBy(Long userId);

    long countByStatus(String status);
}