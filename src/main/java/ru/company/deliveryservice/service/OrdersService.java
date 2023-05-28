package ru.company.deliveryservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.company.deliveryservice.dto.responses.CouriersGroupOrders;
import ru.company.deliveryservice.dto.responses.GroupOrders;
import ru.company.deliveryservice.dto.responses.OrderAssignResponse;
import ru.company.deliveryservice.dto.responses.OrderResponse;
import ru.company.deliveryservice.entity.Courier;
import ru.company.deliveryservice.entity.Order;
import ru.company.deliveryservice.optionally.CourierOptional;
import ru.company.deliveryservice.repository.CouriersRepository;
import ru.company.deliveryservice.repository.OrdersRepository;
import ru.company.deliveryservice.dto.requests.CompleteOrder;

import java.time.LocalTime;
import java.util.*;

@Service
public class OrdersService {

    public static long idOrder = 1;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    CouriersRepository couriersRepository;

    @Transactional
    public List<Order> CompleteOrders(List<CompleteOrder> completeInfo) {
        Map<Long, Order> prevOrdersInRequest = new HashMap<>();
        for (CompleteOrder info: completeInfo) {
            Long orderIdRequest = info.getOrderId();
            Long courierIdRequest = info.getCourierId();
             if (!ordersRepository.existsById(orderIdRequest))
                 return null;
             Order order = ordersRepository.getReferenceById(orderIdRequest);
             if (order.getCourierCompletedId() != null
                     && !(order.getCourierCompletedId().getId().equals(courierIdRequest)
                     && order.getCompletedTime().equals(info.getCompleteTime())))
                 return null;
             if (!couriersRepository.existsById(courierIdRequest))
                return null;
             if (prevOrdersInRequest.containsKey(orderIdRequest)) {
                 Order prevOrder = prevOrdersInRequest.get(orderIdRequest);
                 if (!(prevOrder.getCourierCompletedId().equals(info.getCourierId())
                         && prevOrder.getCompletedTime().equals(info.getCompleteTime())))
                     return null;
             }
             prevOrdersInRequest.put(orderIdRequest, order);
        }


        //СКОРЕЕ ВСЕГО НЕПРАВИЛЬНО, ИЗЗА MANYTOMANY
        //заполняю данные
        List<Order> result = new ArrayList<>();
        for (CompleteOrder info: completeInfo) {
            Order order = ordersRepository.getReferenceById(info.getOrderId());
            Courier courier = couriersRepository.getReferenceById(info.getCourierId());
            order.setCourierCompletedId(courier);
            order.setCompletedTime(info.getCompleteTime());
            result.add(order);
        }
        ordersRepository.saveAll(result);

        return result;
    }

    @Transactional
    public OrderAssignResponse assignCouriers() {
        OrderAssignResponse response = new OrderAssignResponse();
        List<CouriersGroupOrders> couriersGroupOrders = new ArrayList<>();
        ////////////////////////////////////////////////ARRAY LIST!!!!!

        List<Order> orders = ordersRepository.getOrdersByCourierCompletedIdIsNull();
        List<Courier> couriers = couriersRepository.findAll();

        orders.sort((o1, o2) -> o1.getCost() - o2.getCost());

        List<CourierOptional> footCouriers = couriers.stream().filter(t -> "FOOT".equals(t.getType()))
                .map(CourierOptional::new).toList();
        List<CourierOptional> bikeCouriers = couriers.stream().filter(t -> "BIKE".equals(t.getType()))
                .map(CourierOptional::new).toList();
        List<CourierOptional> autoCouriers = couriers.stream().filter(t -> "AUTO".equals(t.getType()))
                .map(CourierOptional::new).toList();
        boolean flag = true;
        while(flag) {
            boolean courierAssigned = false;
            for (CourierOptional courier : autoCouriers) {
                Double totalWeight = 40.0;
                Set<Integer> visitedRegions = new HashSet<>();
                Order order = getOrderWithLowestCost(orders, courier, totalWeight, LocalTime.of(0, 8));
                if (order == null)
                    continue;
                courierAssigned = true;


                GroupOrders groupOrders = new GroupOrders(order.getId());
                List<OrderResponse> orderList = new ArrayList<>();
                orderList.add(new OrderResponse(order));


                order.setCourierCompletedId(couriersRepository.getReferenceById(courier.getId()));
                visitedRegions.add(order.getRegion().getType());
                totalWeight -= order.getWeight();
                //////
                for (int i = 0; i < 6; i++) {
                    Order order1 = getOrderWithHighestCost(orders, courier, totalWeight,
                            LocalTime.of(0, 4), visitedRegions, 3);
                    if (order1 == null) {
                        break;
                    }

                    orderList.add(new OrderResponse(order));

                    order1.setCourierCompletedId(couriersRepository.getReferenceById(courier.getId()));
                    totalWeight -= order.getWeight();
                }

                groupOrders.setOrders(orderList);
                boolean courierExists = false;
                for (CouriersGroupOrders courierWithGroups : couriersGroupOrders) {
                    if (courierWithGroups.getCourierId().equals(courier.getId())) {
                        courierWithGroups.getOrders().add(groupOrders);
                        courierExists = true;
                        break;
                    }
                }
                List<GroupOrders> groupOrdersForCourier = new ArrayList<>();
                groupOrdersForCourier.add(groupOrders);
                if (!courierExists) {
                    couriersGroupOrders.add(new CouriersGroupOrders(courier.getId(), groupOrdersForCourier));
                }
            }
            if (!courierAssigned)
                flag = false;
        }
        flag = true;
        while(flag) {
            boolean courierAssigned = false;
            for (CourierOptional courier : bikeCouriers) {
                Double totalWeight = 20.0;
                Set<Integer> visitedRegions = new HashSet<>();
                Order order = getOrderWithLowestCost(orders, courier, totalWeight, LocalTime.of(0, 12));
                if (order == null)
                    continue;

                courierAssigned = true;

                GroupOrders groupOrders = new GroupOrders(order.getId());
                List<OrderResponse> orderList = new ArrayList<>();
                orderList.add(new OrderResponse(order));

                order.setCourierCompletedId(couriersRepository.getReferenceById(courier.getId()));
                visitedRegions.add(order.getRegion().getType());
                totalWeight -= order.getWeight();
                //////
                for (int i = 0; i < 3; i++) {
                    Order order1 = getOrderWithHighestCost(orders, courier, totalWeight,
                            LocalTime.of(0, 8), visitedRegions, 2);
                    if (order1 == null) {
                        break;
                    }

                    orderList.add(new OrderResponse(order));

                    order1.setCourierCompletedId(couriersRepository.getReferenceById(courier.getId()));
                    totalWeight -= order.getWeight();
                }

                groupOrders.setOrders(orderList);
                boolean courierExists = false;
                for (CouriersGroupOrders courierWithGroups : couriersGroupOrders) {
                    if (courierWithGroups.getCourierId().equals(courier.getId())) {
                        courierWithGroups.getOrders().add(groupOrders);
                        courierExists = true;
                        break;
                    }
                }
                List<GroupOrders> groupOrdersForCourier = new ArrayList<>();
                groupOrdersForCourier.add(groupOrders);
                if (!courierExists) {
                    couriersGroupOrders.add(new CouriersGroupOrders(courier.getId(), groupOrdersForCourier));
                }
            }

            if (!courierAssigned)
                flag = false;
        }

        flag = true;
        while(flag) {
            boolean courierAssigned = false;
            for (CourierOptional courier : footCouriers) {
                Double totalWeight = 10.0;
                Set<Integer> visitedRegions = new HashSet<>();
                Order order = getOrderWithLowestCost(orders, courier, totalWeight, LocalTime.of(0, 25));
                if (order == null)
                    continue;

                courierAssigned = true;

                GroupOrders groupOrders = new GroupOrders(order.getId());
                List<OrderResponse> orderList = new ArrayList<>();
                orderList.add(new OrderResponse(order));

                order.setCourierCompletedId(couriersRepository.getReferenceById(courier.getId()));
                visitedRegions.add(order.getRegion().getType());
                totalWeight -= order.getWeight();
                //////
                for (int i = 0; i < 1; i++) {
                    Order order1 = getOrderWithHighestCost(orders, courier, totalWeight,
                            LocalTime.of(0, 10), visitedRegions, 1);
                    if (order1 == null) {
                        break;
                    }

                    orderList.add(new OrderResponse(order));

                    order1.setCourierCompletedId(couriersRepository.getReferenceById(courier.getId()));
                    totalWeight -= order.getWeight();
                }

                groupOrders.setOrders(orderList);
                boolean courierExists = false;
                for (CouriersGroupOrders courierWithGroups : couriersGroupOrders) {
                    if (courierWithGroups.getCourierId().equals(courier.getId())) {
                        courierWithGroups.getOrders().add(groupOrders);
                        courierExists = true;
                        break;
                    }
                }
                List<GroupOrders> groupOrdersForCourier = new ArrayList<>();
                groupOrdersForCourier.add(groupOrders);
                if (!courierExists) {
                    couriersGroupOrders.add(new CouriersGroupOrders(courier.getId(), groupOrdersForCourier));
                }
            }
            if (!courierAssigned)
                flag = false;
        }

        ordersRepository.saveAll(orders);
        response.setCouriers(couriersGroupOrders);
        return response;
    }

    private Order getOrderWithLowestCost(List<Order> orders, CourierOptional courier,
                                         Double availableWeight, LocalTime timeRequired) {
        for (Order order: orders) {
            if (order.getCourierCompletedId() == null && order.getWeight() <= availableWeight && courier.getRegions().contains(order.getRegion().getType())
                                && courier.hasTimeForOrder(timeRequired)) {
                return order;
            }
        }
        return null;
    }

    private Order getOrderWithHighestCost(List<Order> orders, CourierOptional courier,
                                         Double availableWeight, LocalTime timeRequired, Set<Integer> regions, Integer maxCountRegions) {
        for (int i = orders.size() - 1; i > -1; i--) {
            if (orders.get(i).getCourierCompletedId() == null && orders.get(i).getWeight() <= availableWeight) {
                if (regions.contains(orders.get(i).getRegion().getType()) || regions.size() < maxCountRegions) {
                    if (courier.hasTimeForOrder(timeRequired)) {
                        regions.add(orders.get(i).getRegion().getType());
                        return orders.get(i);
                    }
                }
            }
        }
        return null;
    }
}
