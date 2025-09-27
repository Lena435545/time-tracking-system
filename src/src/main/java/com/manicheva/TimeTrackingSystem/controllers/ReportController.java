package com.manicheva.TimeTrackingSystem.controllers;

import com.manicheva.TimeTrackingSystem.services.PdfService;
import com.manicheva.TimeTrackingSystem.services.TimeEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final PdfService pdfService;
    private final TimeEntryService timeEntryService;

    public ReportController(PdfService pdfService, TimeEntryService timeEntryService) {
        this.pdfService = pdfService;
        this.timeEntryService = timeEntryService;
    }

    @GetMapping
    public String showReportsPage() {
        return "reports/reports";
    }


    @GetMapping("/daily")
    public ResponseEntity<byte[]> downloadDailyReport(
            @RequestParam(name="date", required=false) String date
    ) throws Exception {
        LocalDate localDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        var user = timeEntryService.getCurrentUser();

        byte[] pdfBytes = pdfService.generateDailyReport(user, localDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + localDate + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }


    @GetMapping("/ihk")
    public ResponseEntity<byte[]> downloadIhkReport(
            @RequestParam(name="weekStart") String weekStart
    ) throws Exception {
        LocalDate startDate = LocalDate.parse(weekStart);
        var user = timeEntryService.getCurrentUser();

        byte[] pdfBytes = pdfService.generateWeeklyIHKReport(user, startDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ihk_report_" + startDate + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}

