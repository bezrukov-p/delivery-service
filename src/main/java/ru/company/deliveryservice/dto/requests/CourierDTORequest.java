package ru.company.deliveryservice.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class CourierDTORequest {
    @JsonProperty("courier_type")
    private String type;
    @JsonProperty("working_hours")
    private List<String> workingHours;
    private List<Integer> regions;
}
