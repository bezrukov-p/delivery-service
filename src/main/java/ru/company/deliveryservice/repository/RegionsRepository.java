package ru.company.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.company.deliveryservice.entity.Region;

@Repository
public interface RegionsRepository extends JpaRepository<Region, Integer> {
}
