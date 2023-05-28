package ru.company.deliveryservice.optionally;

import lombok.Data;
import ru.company.deliveryservice.entity.Courier;
import ru.company.deliveryservice.entity.Hours;
import ru.company.deliveryservice.entity.Region;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CourierOptional {
    private Long id;
    private String type;
    private List<Integer> regions;
    private List<StartAndEndTime> workingHours = new ArrayList<>();

    public CourierOptional(Courier courier) {
        this.id = courier.getId();
        this.type = courier.getType();
        this.regions = courier.getRegions().stream().map(Region::getType).toList();
        for (String hours: courier.getWorkingHours().stream().map(Hours::getHours).toList()) {
            workingHours.add(new StartAndEndTime(hours));
        }
    }

    public boolean hasTimeForOrder(LocalTime timeRequired) {
        for (StartAndEndTime hours: workingHours) {
            if (hours.hasFreeTime(timeRequired))
                return true;
        }
        return false;
    }
}
