package ru.company.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.company.deliveryservice.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM (\n" +
                    "SELECT *, row_number() over (ORDER BY id) as rownum FROM orders\n" +
                    ") as table_with_rownum\n" +
                    "WHERE rownum > ? LIMIT ?;")
    List<Order> getOrdersByOffsetAndLimit(Integer offset, Integer limit);

    List<Order> findOrdersByCourierCompletedId_IdAndCompletedTimeGreaterThanEqualAndCompletedTimeLessThan(
            Long courierCompletedId_id, LocalDateTime completedTime, LocalDateTime completedTime2);
    List<Order> getOrdersByCourierCompletedIdIsNull();
}
