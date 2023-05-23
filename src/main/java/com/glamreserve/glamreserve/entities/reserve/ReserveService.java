package com.glamreserve.glamreserve.entities.reserve;

import com.glamreserve.glamreserve.entities.dto.AvailableSlot;
import com.glamreserve.glamreserve.entities.schedule.Schedule;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class ReserveService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ReserveRepository reservationRepository;

    public List<AvailableSlot> getAvailableSlots(LocalDate date, Long companyId) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        List<Schedule> schedules = scheduleRepository.findByCompanyIdAndDayOfWeek(companyId, dayOfWeek);
        List<Reserve> reservations = reservationRepository.findByCompanyAndDate(companyId, date);

        return schedules.stream()
                .flatMap(schedule -> Stream.concat(
                        findAvailableSlotsInPeriod(schedule.getOpenTime1(), schedule.getCloseTime1(), reservations).stream(),
                        findAvailableSlotsInPeriod(schedule.getOpenTime2(), schedule.getCloseTime2(), reservations).stream()))
                .collect(Collectors.toList());
    }

    private List<AvailableSlot> findAvailableSlotsInPeriod(Time openTime, Time closeTime, List<Reserve> reservations) {
        List<AvailableSlot> availableSlots = new ArrayList<>();
        long intervalInMinutes = 30L;

        Instant openInstant = openTime.toInstant();
        Instant closeInstant = closeTime.toInstant();

        Instant startTime = openInstant;
        Instant endTime = startTime.plus(Duration.ofMinutes(intervalInMinutes));

        while (endTime.isBefore(closeInstant) || endTime.equals(closeInstant)) {
            Instant finalStartTime = startTime;
            Instant finalEndTime = endTime;
            boolean isSlotFree = reservations.stream().noneMatch(reservation -> {
                Instant reservationInstant = reservation.getDate().toInstant();
                return (reservationInstant.equals(finalStartTime) || reservationInstant.isAfter(finalStartTime))
                        && reservationInstant.isBefore(finalEndTime);
            });

            if (isSlotFree) {
                availableSlots.add(new AvailableSlot(
                        startTime.toString(),
                        endTime.toString()));
            }

            startTime = endTime;
            endTime = startTime.plus(Duration.ofMinutes(intervalInMinutes));
        }

        return availableSlots;
    }
}
