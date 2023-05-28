package ru.company.deliveryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.company.deliveryservice.entity.Courier;
import ru.company.deliveryservice.entity.Order;
import ru.company.deliveryservice.repository.CouriersRepository;
import ru.company.deliveryservice.repository.OrdersRepository;
import ru.company.deliveryservice.dto.responses.CourierMetaInfoResponse;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CouriersService {
    @Autowired
    private CouriersRepository couriersRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public CourierMetaInfoResponse getMetaInfoForCourier(Long courierId, LocalDate startDate, LocalDate endDate) {
        List<Order> orders =  ordersRepository.
                findOrdersByCourierCompletedId_IdAndCompletedTimeGreaterThanEqualAndCompletedTimeLessThan(courierId, startDate.atStartOfDay(), endDate.atStartOfDay());
        Courier courier = couriersRepository.getReferenceById(courierId);
        int hoursBetweenDates = (int)Duration.between(
                LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MIN)).toHours();

        CourierMetaInfoResponse result = new CourierMetaInfoResponse(courier);
        int coeff1 = "FOOT".equals(courier.getType()) ? 2 : "BIKE".equals(courier.getType()) ? 3 : 4;
        int coeff2 = "FOOT".equals(courier.getType()) ? 3 : "BIKE".equals(courier.getType()) ? 2 : 1;
        if (!orders.isEmpty()) {
            int earning = orders.stream().mapToInt(Order::getCost).map(cost -> cost * coeff1).sum();
            int rating = Math.round(((float)(orders.size() * coeff2) / hoursBetweenDates));
            result.setEarnings(earning);
            result.setRating(rating);
        }

        return result;
    }
}
