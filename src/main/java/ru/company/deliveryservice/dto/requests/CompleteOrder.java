package ru.company.deliveryservice.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompleteOrder {
    @JsonProperty("courier_id")
    private Long courierId;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("complete_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime completeTime;
}
