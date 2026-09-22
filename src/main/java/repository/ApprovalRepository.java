package com.indiapost.brsrplatform.repository;

import com.indiapost.brsrplatform.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByReportId(Long reportId);

    List<Approval> findByReviewedBy(Long userId);
}