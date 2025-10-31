package lk.jiat.ee.savoryhubproject.service;

import lk.jiat.ee.savoryhubproject.dto.DashboardStatsDTO;
import lk.jiat.ee.savoryhubproject.repo.OrderRepository;
import lk.jiat.ee.savoryhubproject.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public DashboardService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalOrders(orderRepository.count());
        stats.setPendingOrders(orderRepository.countByStatus("PENDING"));

        Double totalRevenue = orderRepository.getTotalRevenue();
        stats.setTotalRevenue(totalRevenue == null ? 0.0 : totalRevenue);

        return stats;
    }
}