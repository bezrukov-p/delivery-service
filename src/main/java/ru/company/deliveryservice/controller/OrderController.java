package ru.company.deliveryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.company.deliveryservice.entity.Order;
import ru.company.deliveryservice.repository.HoursRepository;
import ru.company.deliveryservice.repository.OrdersRepository;
import ru.company.deliveryservice.repository.RegionsRepository;
import ru.company.deliveryservice.dto.requests.CompleteOrderRequestDTO;
import ru.company.deliveryservice.dto.requests.CreateOrderRequest;
import ru.company.deliveryservice.dto.responses.OrderResponse;
import ru.company.deliveryservice.service.OrdersService;
import ru.company.deliveryservice.tools.LimitedEndpoint;
import ru.company.deliveryservice.tools.ValidatorTool;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private HoursRepository hoursRepository;
    @Autowired
    private ValidatorTool validator;

    @PostMapping("/orders")
    @Transactional
    @LimitedEndpoint
    public ResponseEntity<?> createOrder(@RequestBody(required = false) String json) {
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{}");
        }
        if (!validator.isValid(node, "CreateOrderRequest"))
            return ResponseEntity.badRequest().body("{}");

        CreateOrderRequest request;
        try {
            request = mapper.readValue(json, new TypeReference<CreateOrderRequest>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Object());
        }

        List<Order> orders = request.getOrders().stream().map(Order::new).toList();
        orders.stream().map(Order::getDeliveryHours).forEach(hoursRepository::saveAll);
        orders.stream().map(Order::getRegion).forEach(regionsRepository::save);
        ordersRepository.saveAll(orders);
        List<OrderResponse> ordersResponse = orders.stream().map(OrderResponse::new).toList();

        return ResponseEntity.ok(ordersResponse);

    }

    @GetMapping("/orders/{order_id}")
    @LimitedEndpoint
    public ResponseEntity<?> getOrder(@PathVariable String order_id) {
        Long id;
        try {
            id = Long.parseLong(order_id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("{}");
        }
        if (!ordersRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");

        Order order = ordersRepository.getReferenceById(id);
        OrderResponse response = new OrderResponse(order);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    @LimitedEndpoint
    public ResponseEntity<?> getOrders(@RequestParam(required = false, name = "limit") String limit,
                                       @RequestParam(required = false, name = "offset") String offset) {
        Integer limitNum;
        Integer offsetNum;
        try {
            limitNum = limit == null || limit.isEmpty() ? 1 : Integer.parseInt(limit);
            offsetNum = offset == null || offset.isEmpty() ? 0 : Integer.parseInt(offset);
        }
        catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (limitNum < 1 || offsetNum < 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        List<Order> orders = ordersRepository.getOrdersByOffsetAndLimit(offsetNum, limitNum);
        List<OrderResponse> ordersResponse = orders.stream().map(OrderResponse::new).toList();
        return ResponseEntity.ok(ordersResponse);
    }

    @PostMapping("/orders/complete")
    @LimitedEndpoint
    public ResponseEntity<?> completeOrder(@RequestBody(required = false) String json) {
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{}");
        }
        if (!validator.isValid(node, "CompleteOrderRequestDto"))
            return ResponseEntity.badRequest().body("{}");

        CompleteOrderRequestDTO request;
        try {
            request = mapper.readValue(json, new TypeReference<CompleteOrderRequestDTO>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{}");
        }


        List<Order> completedOrders = ordersService.CompleteOrders(request.getCompleteInfo());
        if (completedOrders == null)
            return ResponseEntity.badRequest().body("{}");
        List<OrderResponse> response = completedOrders.stream().map(OrderResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/orders/assign")
    @LimitedEndpoint
    public ResponseEntity<?> ordersAssign(@RequestParam(required = false) String date) {
        return ResponseEntity.status(501).build();
    }

}
