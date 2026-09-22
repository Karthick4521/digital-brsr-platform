package com.indiapost.brsrplatform.controller;

import com.indiapost.brsrplatform.service.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportExportController {

    @Autowired
    private ReportGenerationService reportGenerationService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPDF() {
        try {
            byte[] pdfBytes = reportGenerationService.generatePDF();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"BRSR_Report.pdf\"")
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel() {
        try {
            byte[] excelBytes = reportGenerationService.generateExcel();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument" +
                                    ".spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"BRSR_Report.xlsx\"")
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}