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
import ru.company.deliveryservice.dto.responses.*;
import ru.company.deliveryservice.entity.Courier;
import ru.company.deliveryservice.repository.CouriersRepository;
import ru.company.deliveryservice.repository.HoursRepository;
import ru.company.deliveryservice.repository.RegionsRepository;
import ru.company.deliveryservice.dto.requests.CreateCourierRequest;
import ru.company.deliveryservice.service.CouriersService;
import ru.company.deliveryservice.tools.LimitedEndpoint;
import ru.company.deliveryservice.tools.ValidatorTool;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class CourierController {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CouriersService couriersService;
    @Autowired
    private CouriersRepository couriersRepository;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private HoursRepository hoursRepository;

    @Autowired
    private ValidatorTool validator;


    @PostMapping("/couriers")
    @Transactional
    @LimitedEndpoint
    public ResponseEntity<?> createCourier(@RequestBody(required = false) String json) {
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{}");
        }

        if (!validator.isValid(node, "CreateCourierRequest"))
            return ResponseEntity.badRequest().body("{}");

        CreateCourierRequest request;
        try {
            request = mapper.readValue(json, new TypeReference<CreateCourierRequest>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Object());
        }

        List<Courier> couriers = request.getCouriers().stream().map(Courier::new).toList();
        couriers.stream().map(Courier::getRegions).forEach(regionsRepository::saveAll);
        couriers.stream().map(Courier::getWorkingHours).forEach(hoursRepository::saveAll);
        couriersRepository.saveAll(couriers);
        List<CourierResponse> responseCouriers = couriers.stream().map(CourierResponse::new).toList();
        CreateCouriersResponse response = new CreateCouriersResponse(responseCouriers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/couriers/{courier_id}")
    @LimitedEndpoint
    public ResponseEntity<?> getCourierById(@PathVariable String courier_id) throws InterruptedException {
        Long id;
        try {
            id = Long.parseLong(courier_id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("{}");
        }

        if (!couriersRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");

        Courier courier = couriersRepository.getReferenceById(id);
        CourierResponse response = new CourierResponse(courier);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/couriers")
    @LimitedEndpoint
    public ResponseEntity<?> getCouriers(@RequestParam(required = false, name = "limit") String limit,
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

        List<Courier> couriers = couriersRepository.getCouriersByOffsetAndLimit(offsetNum, limitNum);
        List<CourierResponse> couriersResponse = couriers.stream().map(CourierResponse::new).toList();
        CouriersResponseWithLimitOffset response =
                new CouriersResponseWithLimitOffset(couriersResponse, offsetNum, limitNum);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/couriers/meta-info/{courier_id}")
    @LimitedEndpoint
    public ResponseEntity<?> getCourierMetaInfo(@PathVariable("courier_id") String courierIdIn,
                                   @RequestParam(required = false,name = "startDate") String startDateIn,
                                   @RequestParam(required = false, name = "endDate") String endDateIn) {
        if (startDateIn == null || endDateIn == null)
            return ResponseEntity.badRequest().body("{}");

        LocalDate startDate;
        LocalDate endDate;
        Long courierId;
        try {
            courierId = Long.parseLong(courierIdIn);
            startDate = LocalDate.parse(startDateIn);
            endDate = LocalDate.parse(endDateIn);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("{}");
        }
        CourierMetaInfoResponse response = couriersService.getMetaInfoForCourier(courierId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/couriers/assigment")
    @LimitedEndpoint
    public ResponseEntity<?> couriersAssignments(@RequestParam String date, @RequestParam(value = "courier_id") String courierId) {
        return ResponseEntity.status(501).build();
    }
}
