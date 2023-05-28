package ru.company.deliveryservice.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CouriersResponseWithLimitOffset {
    private List<CourierResponse> couriers = new ArrayList<>();
    private Integer offset;
    private Integer limit;

    public CouriersResponseWithLimitOffset(List<CourierResponse> couriers, Integer offset, Integer limit) {
        this.couriers = couriers;
        this.limit = limit;
        this.offset = offset;
    }
}
