package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.contact.Contact;
import com.glamreserve.glamreserve.entities.contact.ContactRepository;
import com.glamreserve.glamreserve.entities.service.Service;
import com.glamreserve.glamreserve.entities.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
public class adminContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/admin/adminContacts")
    public ModelAndView adminContact() {
        ModelAndView modelAndView = new ModelAndView("admin/adminContacts");
        Contact contact = new Contact();
        modelAndView.addObject("contact", contact);
        List<Contact> contacts = contactRepository.findAll();
        modelAndView.getModelMap().addAttribute("contacts", contacts);
        return modelAndView;
    }
    @PostMapping("/adminContactAdd")
    public ModelAndView adminContactAdd (@ModelAttribute("service") Contact contact, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminContacts");
       // Service exitingUser = serviceRepository.findByUsername(service.getUsername());
       // if(exitingUser==null) {
        contactRepository.save(contact);
            atri.addFlashAttribute("success", "Contacto guardado con éxito");
       // }else {
       //     atri.addFlashAttribute("error", "Usuario no actualizado,El nombre de usuario ya existe");
       // }
        return modelo;
    }

    @PostMapping("/adminContactUpdate")
    public ModelAndView adminContactUpdate (@ModelAttribute("service") Contact contact, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminContacts");
        contactRepository.save(contact);

            atri.addFlashAttribute("success", "Contacto actualizado con éxito");

            return modelo;
    }



   @GetMapping("/adminContactUpdateForm/{id}")
    public ModelAndView updateContact(@PathVariable(name="id") Long id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminContactUpdateForm");
       Contact contact = contactRepository.findById(id).get();

       modelo.addObject("contact",contact);

        return modelo;
    }

    @GetMapping("/deleteContact/{id}")
    public ModelAndView deleteService(@PathVariable(name="id") Long id, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminContacts");

        contactRepository.deleteById(id);
        atri.addFlashAttribute("success", "Contacto eliminado con éxito");


        return modelo;
    }


}
