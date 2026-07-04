package com.indiapost.brsrplatform.repository;

import com.indiapost.brsrplatform.entity.ReportDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReportDetailsRepository
        extends JpaRepository<ReportDetails, Long> {

    Optional<ReportDetails> findByReportId(Long reportId);
}