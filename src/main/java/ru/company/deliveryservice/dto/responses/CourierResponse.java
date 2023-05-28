package ru.company.deliveryservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.company.deliveryservice.entity.Courier;
import ru.company.deliveryservice.entity.Hours;
import ru.company.deliveryservice.entity.Region;

import java.util.List;

@Data
public class CourierResponse {
    @JsonProperty("courier_id")
    private Long courierId;
    @JsonProperty("courier_type")
    private String courierType;
    private List<Integer> regions;
    @JsonProperty("working_hours")
    private List<String> hours;

    public CourierResponse(Courier courier) {
        courierId = courier.getId();
        courierType = courier.getType();
        regions = courier.getRegions().stream().map(Region::getType).toList();
        hours = courier.getWorkingHours().stream().map(Hours::getHours).toList();
    }
}
