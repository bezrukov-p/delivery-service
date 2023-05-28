package ru.company.deliveryservice.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompleteOrderRequestDTO {
    @JsonProperty("complete_info")
    List<CompleteOrder> completeInfo = new ArrayList<>();
}
