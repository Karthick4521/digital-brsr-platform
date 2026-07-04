package com.indiapost.brsrplatform.service;

import com.indiapost.brsrplatform.entity.ReportDetails;
import com.indiapost.brsrplatform.entity.SustainabilityReport;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportGenerationService {

    @Autowired
    private SustainabilityReportService reportService;

    public byte[] generatePDF() throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 20,
                com.itextpdf.text.Font.BOLD,
                new BaseColor(200, 16, 46));
        Paragraph title = new Paragraph("India Post - BRSR Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        com.itextpdf.text.Font subFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12,
                com.itextpdf.text.Font.NORMAL, BaseColor.GRAY);
        Paragraph subtitle = new Paragraph(
                "Business Responsibility & Sustainability Reporting", subFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10,
                com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 9);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        String[] headers = {"Report ID", "Financial Year",
                "Department ID", "Status", "Created Date"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new BaseColor(200, 16, 46));
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        List<SustainabilityReport> reports = reportService.getAllReports();
        for (SustainabilityReport r : reports) {
            table.addCell(new Phrase(String.valueOf(r.getId()), cellFont));
            table.addCell(new Phrase(r.getFinancialYear(), cellFont));
            table.addCell(new Phrase(String.valueOf(r.getDepartmentId()), cellFont));
            table.addCell(new Phrase(r.getStatus(), cellFont));
            table.addCell(new Phrase(r.getCreatedAt().toString(), cellFont));
        }
        document.add(table);
        document.add(Chunk.NEWLINE);

        com.itextpdf.text.Font sectionFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12,
                com.itextpdf.text.Font.BOLD, new BaseColor(200, 16, 46));

        for (SustainabilityReport r : reports) {
            ReportDetails d = reportService.getDetailsByReportId(r.getId());
            if (d != null) {
                document.add(new Paragraph(
                        "Report #" + r.getId() + " - " + r.getFinancialYear(),
                        sectionFont));
                document.add(new Paragraph("Energy: "
                        + d.getEnergyConsumption() + " GJ", cellFont));
                document.add(new Paragraph("Water: "
                        + d.getWaterConsumption() + " KL", cellFont));
                document.add(new Paragraph("Waste Generated: "
                        + d.getWasteGenerated() + " MT", cellFont));
                document.add(new Paragraph("Employees: "
                        + d.getTotalEmployees(), cellFont));
                document.add(new Paragraph("Carbon Emission: "
                        + d.getCarbonEmission() + " tCO2e", cellFont));
                document.add(Chunk.NEWLINE);
            }
        }
        document.close();
        return out.toByteArray();
    }

    public byte[] generateExcel() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(
                    IndexedColors.DARK_RED.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Sheet sheet1 = workbook.createSheet("Reports Summary");
            Row headerRow = sheet1.createRow(0);
            String[] headers = {"Report ID", "Financial Year",
                    "Department ID", "Status", "Created Date"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet1.setColumnWidth(i, 5000);
            }

            List<SustainabilityReport> reports = reportService.getAllReports();
            int rowNum = 1;
            for (SustainabilityReport r : reports) {
                Row row = sheet1.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId());
                row.createCell(1).setCellValue(r.getFinancialYear());
                row.createCell(2).setCellValue(r.getDepartmentId());
                row.createCell(3).setCellValue(r.getStatus());
                row.createCell(4).setCellValue(r.getCreatedAt().toString());
            }

            Sheet sheet2 = workbook.createSheet("Environmental Data");
            Row envHeader = sheet2.createRow(0);
            String[] envHeaders = {"Report ID", "Financial Year",
                    "Energy (GJ)", "Water (KL)",
                    "Waste Generated (MT)", "Waste Recycled (MT)",
                    "Carbon Emission (tCO2e)", "Renewable Energy %"};
            for (int i = 0; i < envHeaders.length; i++) {
                Cell cell = envHeader.createCell(i);
                cell.setCellValue(envHeaders[i]);
                cell.setCellStyle(headerStyle);
                sheet2.setColumnWidth(i, 5000);
            }

            int envRow = 1;
            for (SustainabilityReport r : reports) {
                ReportDetails d = reportService.getDetailsByReportId(r.getId());
                if (d != null) {
                    Row row = sheet2.createRow(envRow++);
                    row.createCell(0).setCellValue(r.getId());
                    row.createCell(1).setCellValue(r.getFinancialYear());
                    row.createCell(2).setCellValue(d.getEnergyConsumption() != null ? d.getEnergyConsumption() : 0);
                    row.createCell(3).setCellValue(d.getWaterConsumption() != null ? d.getWaterConsumption() : 0);
                    row.createCell(4).setCellValue(d.getWasteGenerated() != null ? d.getWasteGenerated() : 0);
                    row.createCell(5).setCellValue(d.getWasteRecycled() != null ? d.getWasteRecycled() : 0);
                    row.createCell(6).setCellValue(d.getCarbonEmission() != null ? d.getCarbonEmission() : 0);
                    row.createCell(7).setCellValue(d.getRenewableEnergyPercent() != null ? d.getRenewableEnergyPercent() : 0);
                }
            }

            Sheet sheet3 = workbook.createSheet("Employee Data");
            Row empHeader = sheet3.createRow(0);
            String[] empHeaders = {"Report ID", "Financial Year",
                    "Total Employees", "Training Hours",
                    "Governance Meetings", "Remarks"};
            for (int i = 0; i < empHeaders.length; i++) {
                Cell cell = empHeader.createCell(i);
                cell.setCellValue(empHeaders[i]);
                cell.setCellStyle(headerStyle);
                sheet3.setColumnWidth(i, 5000);
            }

            int empRow = 1;
            for (SustainabilityReport r : reports) {
                ReportDetails d = reportService.getDetailsByReportId(r.getId());
                if (d != null) {
                    Row row = sheet3.createRow(empRow++);
                    row.createCell(0).setCellValue(r.getId());
                    row.createCell(1).setCellValue(r.getFinancialYear());
                    row.createCell(2).setCellValue(d.getTotalEmployees() != null ? d.getTotalEmployees() : 0);
                    row.createCell(3).setCellValue(d.getEmployeeTrainingHours() != null ? d.getEmployeeTrainingHours() : 0);
                    row.createCell(4).setCellValue(d.getGovernanceMeetings() != null ? d.getGovernanceMeetings() : 0);
                    row.createCell(5).setCellValue(d.getRemarks() != null ? d.getRemarks() : "");
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}