package com.glamreserve.glamreserve.entities.schedule;

import com.glamreserve.glamreserve.entities.schedule.Schedule;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleUtils {
    public static List<Schedule> getScheduleForDay(List<Schedule> schedules, int day) {
        return schedules.stream()
                .filter(schedule -> schedule.getDayOfWeek() == day)
                .collect(Collectors.toList());
    }
}
