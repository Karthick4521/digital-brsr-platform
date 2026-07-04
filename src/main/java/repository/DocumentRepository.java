package com.indiapost.brsrplatform.repository;

import com.indiapost.brsrplatform.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByReportId(Long reportId);

    List<Document> findByUploadedBy(Long userId);
}