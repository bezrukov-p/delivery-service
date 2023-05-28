package ru.company.deliveryservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.company.deliveryservice.entity.Hours;
import ru.company.deliveryservice.entity.Order;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class OrderResponse {
    @JsonProperty("order_id")
    private Long id;
    private Double weight;
    @JsonProperty("regions")
    private Integer region;
    private Integer cost;

    @JsonProperty("delivery_hours")
    private List<String> deliveryHours;


    @JsonProperty("completed_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String completedTime;

    public OrderResponse(Order order) {
        id = order.getId();
        weight = order.getWeight();
        region = order.getRegion().getType();
        cost = order.getCost();
        deliveryHours = order.getDeliveryHours().stream().map(Hours::getHours).toList();
        if (order.getCompletedTime() != null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.of(order.getCompletedTime(), ZoneOffset.UTC);
            this.completedTime = zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
