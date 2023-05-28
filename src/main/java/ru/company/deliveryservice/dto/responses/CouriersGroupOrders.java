package ru.company.deliveryservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouriersGroupOrders {
    @JsonProperty("courier_id")
    private Long courierId;
    private List<GroupOrders> orders = new ArrayList<>();

    public CouriersGroupOrders(Long courierId) {
        this.courierId = courierId;
    }

    public void addGroupOrder(GroupOrders groupOrders) {
        orders.add(groupOrders);
    }
}
