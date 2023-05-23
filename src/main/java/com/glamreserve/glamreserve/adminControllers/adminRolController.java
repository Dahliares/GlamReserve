package com.glamreserve.glamreserve.adminControllers;

import com.glamreserve.glamreserve.entities.roles.Role;
import com.glamreserve.glamreserve.entities.roles.RoleRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@RestController
public class adminRolController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/admin/adminRoles")
    public ModelAndView adminRoles() {
        ModelAndView modelAndView = new ModelAndView("admin/adminRoles");
        Role role = new Role();
        modelAndView.addObject("role", role);
        List<Role> roles = roleRepository.findAll();
        modelAndView.getModelMap().addAttribute("roles", roles);
        return modelAndView;
    }
    @PostMapping("/adminRoleAdd")
    public ModelAndView adminRoleAdd (@ModelAttribute("role") Role role, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminRoles");
        roleRepository.save(role);
            atri.addFlashAttribute("success", "Rol guardado con éxito");

        return modelo;
    }

    @PostMapping("/adminRoleUpdate")
    public ModelAndView adminRoleUpdate (@ModelAttribute("role") Role role, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminRoles");
        roleRepository.save(role);
            atri.addFlashAttribute("success", "Rol actualizado con éxito");

            return modelo;
    }



   @GetMapping("/adminRoleUpdateForm/{id}")
    public ModelAndView updateUser(@PathVariable(name="id") Long id, RedirectAttributes atri){
        ModelAndView modelo = new ModelAndView("admin/adminRoleUpdateForm");
        Role role = roleRepository.findById(id).get();

       modelo.addObject("role",role);

        return modelo;
    }

    @GetMapping("/deleteRole/{id}")
    public ModelAndView deleteRol(@PathVariable(name="id") Long id, RedirectAttributes atri){

        ModelAndView modelo = new ModelAndView("redirect:/admin/adminRoles");
        roleRepository.deleteById(id);
            atri.addFlashAttribute("success", "Rol eliminado con éxito");

        return modelo;
    }


}
