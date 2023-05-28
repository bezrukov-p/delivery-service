package ru.company.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "regions")
@Data
@EqualsAndHashCode(of = "type")
public class Region {
    @Id
    @Column(name = "type")
    private Integer type;

    @ManyToMany(mappedBy = "regions")
    private Set<Courier> couriers = new HashSet<>();

    @OneToMany(mappedBy = "region")
    private Set<Order> orders = new HashSet<>();

    public Region(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RegionDB{" +
                "type=" + type +
                '}';
    }
}
