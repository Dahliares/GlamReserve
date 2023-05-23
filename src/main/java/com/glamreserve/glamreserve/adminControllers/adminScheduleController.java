package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.schedule.Schedule;
import com.glamreserve.glamreserve.entities.schedule.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
public class adminScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/admin/adminSchedules")
    public ModelAndView adminSchedule() {
        ModelAndView modelAndView = new ModelAndView("admin/adminSchedules");
        Schedule schedule = new Schedule();
        modelAndView.addObject("schedule", schedule);
        List<Schedule> schedules = scheduleRepository.findAll();
        modelAndView.getModelMap().addAttribute("schedules", schedules);
        return modelAndView;
    }
   @PostMapping("/adminScheduleAdd")
    public ModelAndView adminSchedulewAdd (@ModelAttribute("schedule") Schedule schedule, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminSchedules");

       scheduleRepository.save(schedule);
            atri.addFlashAttribute("success", "Horario guardado con éxito");

        return modelo;
    }

    @PostMapping("/adminScheduleUpdate")
    public ModelAndView adminScheduleUpdate (@ModelAttribute("schedule") Schedule schedule, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminSchedules");

        //Service serviceToUpdate = serviceRepository.getUserById(service.getId());
        scheduleRepository.save(schedule);

            atri.addFlashAttribute("success", "Horario actualizado con éxito");

            return modelo;
    }



   @GetMapping("/adminScheduleUpdateForm/{id}")
    public ModelAndView updateSchedule(@PathVariable(name="id") Long id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminScheduleUpdateForm");
       Schedule schedule = scheduleRepository.findById(id).get();

       modelo.addObject("schedule",schedule);

        return modelo;
    }

    @GetMapping("/deleteSchedule/{id}")
    public ModelAndView deleteSchedule(@PathVariable(name="id") Long id, RedirectAttributes atri){

        //Review review = reviewRepository.findById(id).get();
        ModelAndView modelo = new ModelAndView("redirect:/admin/adminSchedules");

        scheduleRepository.deleteById(id);
            atri.addFlashAttribute("success", "Horario eliminado con éxito");


        return modelo;
    }


}
