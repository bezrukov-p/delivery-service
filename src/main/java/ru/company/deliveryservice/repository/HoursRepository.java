package ru.company.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.company.deliveryservice.entity.Hours;

@Repository
public interface HoursRepository extends JpaRepository<Hours, String> {
}
