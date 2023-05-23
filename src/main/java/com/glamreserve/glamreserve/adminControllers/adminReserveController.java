package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.reserve.Reserve;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;
import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
public class adminReserveController {

    @Autowired
    private ReserveRepository reserveRepository;

    @GetMapping("/admin/adminReserves")
    public ModelAndView adminReserve() {
        ModelAndView modelAndView = new ModelAndView("admin/adminReserves");
        Reserve reserve = new Reserve();
        modelAndView.addObject("reserve", reserve);
        List<Reserve> reserves = reserveRepository.findAll();
        modelAndView.getModelMap().addAttribute("reserves", reserves);
        return modelAndView;
    }
    @PostMapping("/adminReserveAdd")
    public ModelAndView adminReserveAdd (@ModelAttribute("reserve") Reserve reserve, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminReserves");

        reserveRepository.save(reserve);
            atri.addFlashAttribute("success", "Reserva guardado con éxito");

        return modelo;
    }

    @PostMapping("/adminReserveUpdate")
    public ModelAndView adminReserveUpdate (@ModelAttribute("service") Reserve reserve, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminReserves");

        //Service serviceToUpdate = serviceRepository.getUserById(service.getId());
        reserveRepository.save(reserve);

            atri.addFlashAttribute("success", "Reserva actualizada con éxito");

            return modelo;
    }



   @GetMapping("/adminReserveUpdateForm/{id}")
    public ModelAndView updateReserve(@PathVariable(name="id") Long id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminReserveUpdateForm");
       Reserve reserve = reserveRepository.findById(id).get();

       modelo.addObject("reserve",reserve);

        return modelo;
    }

    @GetMapping("/deleteReserve/{id}")
    public ModelAndView deleteReserve(@PathVariable(name="id") Long id, RedirectAttributes atri){

        Reserve reserve = reserveRepository.findById(id).get();
        ModelAndView modelo = new ModelAndView("redirect:/admin/adminReserves");

        reserveRepository.deleteById(id);
            atri.addFlashAttribute("success", "Reserva eliminada con éxito");


        return modelo;
    }


}
