package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.entity.Document;
import com.indiapost.brsrplatform.entity.User;
import com.indiapost.brsrplatform.service.DocumentService;
import com.indiapost.brsrplatform.service.SustainabilityReportService;
import com.indiapost.brsrplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SustainabilityReportService reportService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listDocuments(Model model) {
        model.addAttribute("documents",
                documentService.getAllDocuments());
        model.addAttribute("reports",
                reportService.getAllReports());
        return "documents/list";
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("reports",
                reportService.getAllReports());
        return "documents/upload";
    }

    @PostMapping("/upload")
    public String uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("reportId") Long reportId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Please select a file to upload!");
            return "redirect:/documents/upload";
        }

        try {
            User user = userService.findByEmail(authentication.getName());
            Long uploadedBy = user != null ? user.getId() : 1L;
            documentService.uploadDocument(file, reportId, uploadedBy);
            redirectAttributes.addFlashAttribute("successMsg",
                    "File uploaded successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Upload failed: " + e.getMessage());
        }
        return "redirect:/documents";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Document doc = documentService.getDocumentById(id);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = documentService.getFilePath(id);
        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteDocument(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        documentService.deleteDocument(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Document deleted successfully!");
        return "redirect:/documents";
    }
}