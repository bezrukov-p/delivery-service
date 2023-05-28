package ru.company.deliveryservice.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateCouriersResponse {
    private List<CourierResponse> couriers;

    public CreateCouriersResponse(List<CourierResponse> couriers) {
        this.couriers = couriers;
    }
}
