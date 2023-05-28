package ru.company.deliveryservice.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTORequest {
    private Double weight;
    @JsonProperty("regions")
    private Integer region;
    private Integer cost;
    @JsonProperty("delivery_hours")
    private List<String> deliveryHours = new ArrayList<>();
}
