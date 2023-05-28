package ru.company.deliveryservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.company.deliveryservice.entity.Courier;
import ru.company.deliveryservice.entity.Hours;
import ru.company.deliveryservice.entity.Region;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierMetaInfoResponse {
    @JsonProperty("courier_id")
    private Long courierId;
    @JsonProperty("courier_type")
    private String courierType;
    List<Integer> regions = new ArrayList<>();
    @JsonProperty("working_hours")
    List<String> workingHours = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rating;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer earnings;

    public CourierMetaInfoResponse(Courier courier) {
        courierId = courier.getId();
        courierType = courier.getType();
        regions = courier.getRegions().stream().map(Region::getType).toList();
        workingHours = courier.getWorkingHours().stream().map(Hours::getHours).toList();
    }
}
