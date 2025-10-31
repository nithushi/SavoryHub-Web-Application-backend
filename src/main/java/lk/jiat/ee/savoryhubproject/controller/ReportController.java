package lk.jiat.ee.savoryhubproject.controller;

import lk.jiat.ee.savoryhubproject.dto.DashboardStatsDTO;
import lk.jiat.ee.savoryhubproject.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO> getAnalytics() {
        DashboardStatsDTO stats = reportService.getAnalytics();
        return ResponseEntity.ok(stats);
    }
}