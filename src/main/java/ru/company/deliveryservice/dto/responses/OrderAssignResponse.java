package ru.company.deliveryservice.dto.responses;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderAssignResponse {
    private String date = LocalDate.now().toString();
    private List<CouriersGroupOrders> couriers;
}
