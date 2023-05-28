package ru.company.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.company.deliveryservice.entity.Courier;

import java.util.List;

@Repository
public interface CouriersRepository extends JpaRepository<Courier, Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM (\n" +
                    "SELECT *, row_number() over (ORDER BY id) as rownum FROM couriers\n" +
                    ") as table_with_rownum\n" +
                    "WHERE rownum > ? LIMIT ?;")
    List<Courier> getCouriersByOffsetAndLimit(Integer offset, Integer limit);

}
