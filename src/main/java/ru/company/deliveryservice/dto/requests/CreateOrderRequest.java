package ru.company.deliveryservice.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    List<OrderDTORequest> orders;
}
