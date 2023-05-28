package ru.company.deliveryservice.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CreateCourierRequest {
    List<CourierDTORequest> couriers;
}
