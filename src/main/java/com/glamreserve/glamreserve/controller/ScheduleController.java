package com.glamreserve.glamreserve.controller;

import com.glamreserve.glamreserve.entities.interceptor.ForbiddenAccessException;
import com.glamreserve.glamreserve.entities.schedule.Schedule;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule schedule1 = new Schedule(schedule.getCompany(), schedule.getDayOfWeek());
        Schedule createdSchedule = scheduleRepository.save(schedule1);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable("id") Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Schedule>> getSchedulesByCompanyId(@PathVariable("companyId") Long companyId) {
        List<Schedule> schedules = scheduleRepository.findByCompanyId(companyId);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @RequestBody Schedule schedule) {
        Schedule existingSchedule = scheduleRepository.findById(id).orElse(null);

        if (existingSchedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = AuthenticationService.getUserFromToken(token, userRepository);
        //comprovar que l'usuari sigui propietari de l'empresa a la que correspon l'horari
        if (!Objects.equals(user.getId(), existingSchedule.getCompany().getOwner().getId())) {
            throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
        }
        schedule.setId(Long.valueOf(Math.toIntExact(id)));
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        Schedule existingSchedule = scheduleRepository.findById(id).orElse(null);

        if (existingSchedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = AuthenticationService.getUserFromToken(token, userRepository);
        //comprovar que l'usuari sigui propietari de l'empresa a la que correspon l'horari
        if (!Objects.equals(user.getId(), existingSchedule.getCompany().getOwner().getId())) {
            throw new ForbiddenAccessException("Acceso denegado: No eres dueño de esta empresa");
        }

        scheduleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
