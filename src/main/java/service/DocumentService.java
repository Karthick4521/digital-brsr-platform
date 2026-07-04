package com.indiapost.brsrplatform.service;

import com.indiapost.brsrplatform.entity.Document;
import com.indiapost.brsrplatform.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${file.upload.dir:C:/brsr-uploads}")
    private String uploadDir;

    public List<Document> getDocumentsByReport(Long reportId) {
        return documentRepository.findByReportId(reportId);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public Document uploadDocument(MultipartFile file,
                                   Long reportId,
                                   Long uploadedBy) throws IOException {

        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalName = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID() + "_" + originalName;
        Path filePath = uploadPath.resolve(uniqueName);

        // Save file to disk
        Files.copy(file.getInputStream(), filePath,
                StandardCopyOption.REPLACE_EXISTING);

        // Save to database
        Document document = new Document();
        document.setFileName(originalName);
        document.setFilePath(filePath.toString());
        document.setFileType(file.getContentType());
        document.setReportId(reportId);
        document.setUploadedBy(uploadedBy);

        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        Document doc = getDocumentById(id);
        if (doc != null) {
            // Delete file from disk
            try {
                Files.deleteIfExists(Paths.get(doc.getFilePath()));
            } catch (IOException e) {
                System.out.println("File not found on disk: " + e.getMessage());
            }
            documentRepository.deleteById(id);
        }
    }

    public Path getFilePath(Long id) {
        Document doc = getDocumentById(id);
        if (doc != null) {
            return Paths.get(doc.getFilePath());
        }
        return null;
    }
}