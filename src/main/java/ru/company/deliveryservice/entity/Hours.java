package ru.company.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Table(name = "hours")
@Entity
@EqualsAndHashCode(of = "hours")
public class Hours {
    @Id
    @Column(name = "hours")
    private String hours;
    @ManyToMany(mappedBy = "workingHours")
    private Set<Courier> couriers = new HashSet<>();

    @ManyToMany(mappedBy = "deliveryHours")
    private Set<Order> orders = new HashSet<>();

    public Hours(String hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "HoursDB{" +
                "hours='" + hours + '\'' +
                '}';
    }
}
