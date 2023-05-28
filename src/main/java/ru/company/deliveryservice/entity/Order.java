package ru.company.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.company.deliveryservice.dto.requests.OrderDTORequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EqualsAndHashCode(of = "id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double weight;
    @ManyToOne
    @JoinColumn(name = "region")
    private Region region;
    private Integer cost;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "orders_hours",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "hours"))
    private Set<Hours> deliveryHours = new HashSet<>();

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @ManyToOne
    @JoinColumn(name = "courier_completed_id")
    private Courier courierCompletedId;

    public Order(OrderDTORequest orderDTORequest) {
        weight = orderDTORequest.getWeight();
        region = new Region(orderDTORequest.getRegion());
        cost = orderDTORequest.getCost();
        deliveryHours = orderDTORequest.getDeliveryHours().stream().map(Hours::new).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", weight=" + weight +
                ", region=" + region +
                ", cost=" + cost +
                ", completedTime=" + completedTime +
                '}';
    }
}
