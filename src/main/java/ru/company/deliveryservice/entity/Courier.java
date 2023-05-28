package ru.company.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.company.deliveryservice.dto.requests.CourierDTORequest;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "couriers")
@EqualsAndHashCode(of = "id")
public class Courier {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String type;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "couriers_regions",
            joinColumns = @JoinColumn(name = "courier_id"),
            inverseJoinColumns = @JoinColumn(name = "region_id"))
    private Set<Region> regions = new HashSet<>();
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "couriers_hours",
            joinColumns = @JoinColumn(name = "courier_id"),
            inverseJoinColumns = @JoinColumn(name = "hours"))
    private Set<Hours> workingHours = new HashSet<>();

    @OneToMany(mappedBy = "courierCompletedId")
    private Set<Order> orders;

    public Courier(CourierDTORequest courierDTORequest) {
        type = courierDTORequest.getType();
        regions = courierDTORequest.getRegions().stream().map(Region::new).collect(Collectors.toSet());
        workingHours = courierDTORequest.getWorkingHours().stream().map(Hours::new).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "CourierDB{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
