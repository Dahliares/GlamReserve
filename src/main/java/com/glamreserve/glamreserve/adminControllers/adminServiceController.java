package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
public class adminServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/admin/adminServices")
    public ModelAndView adminService() {
        ModelAndView modelAndView = new ModelAndView("admin/adminServices");
        Service service = new Service();
        modelAndView.addObject("service", service);
        List<Service> services = serviceRepository.findAll();
        modelAndView.getModelMap().addAttribute("services", services);
        return modelAndView;
    }
    @PostMapping("/adminServiceAdd")
    public ModelAndView adminServiceAdd (@ModelAttribute("service") Service service, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminServices");
       // Service exitingUser = serviceRepository.findByUsername(service.getUsername());
       // if(exitingUser==null) {
            serviceRepository.save(service);
            atri.addFlashAttribute("success", "Servicio guardado con éxito");
       // }else {
       //     atri.addFlashAttribute("error", "Usuario no actualizado,El nombre de usuario ya existe");
       // }
        return modelo;
    }

    @PostMapping("/adminServiceUpdate")
    public ModelAndView adminServiceUpdate (@ModelAttribute("service") Service service, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminServices");

        //Service serviceToUpdate = serviceRepository.getUserById(service.getId());
        serviceRepository.save(service);

            atri.addFlashAttribute("success", "Servicio actualizado con éxito");

            return modelo;
    }



   @GetMapping("/adminServiceUpdateForm/{id}")
    public ModelAndView updateService(@PathVariable(name="id") Integer id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminServiceUpdateForm");
       Service service = serviceRepository.findById(id).get();

       modelo.addObject("service",service);

        return modelo;
    }

    @GetMapping("/deleteService/{id}")
    public ModelAndView deleteService(@PathVariable(name="id") Integer id, RedirectAttributes atri){

        Service service = serviceRepository.findById(id).get();
        ModelAndView modelo = new ModelAndView("redirect:/admin/adminServices");

            serviceRepository.deleteById(id);
            atri.addFlashAttribute("success", "Servicio eliminado con éxito");


        return modelo;
    }


}
