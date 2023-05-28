package ru.company.deliveryservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.company.deliveryservice.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupOrders {
    @JsonProperty("group_order_id")
    private Long groupOrderId;

    List<OrderResponse> orders = new ArrayList<>();

    public GroupOrders(Long groupOrderId) {
        this.groupOrderId = groupOrderId;
    }

    public void addOrderResponse(Order order) {
        orders.add(new OrderResponse(order));
    }

}
