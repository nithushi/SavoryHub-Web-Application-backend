package lk.jiat.ee.savoryhubproject.service;

import lk.jiat.ee.savoryhubproject.dto.DashboardStatsDTO;
import lk.jiat.ee.savoryhubproject.repo.OrderRepository;
import lk.jiat.ee.savoryhubproject.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ReportService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public DashboardStatsDTO getAnalytics() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalOrders(orderRepository.count());

        Double totalRevenue = orderRepository.getTotalRevenue();
        stats.setTotalRevenue(totalRevenue == null ? 0.0 : totalRevenue);

        stats.setPendingOrders(orderRepository.countByStatus("PENDING"));

        return stats;
    }
}