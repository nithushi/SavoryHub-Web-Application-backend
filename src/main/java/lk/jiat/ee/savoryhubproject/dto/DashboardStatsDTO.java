package lk.jiat.ee.savoryhubproject.dto;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private long totalUsers;
    private long totalOrders;
    private double totalRevenue;
    private long pendingOrders;
}